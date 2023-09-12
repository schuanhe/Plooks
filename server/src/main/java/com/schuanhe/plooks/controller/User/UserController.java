package com.schuanhe.plooks.controller.User;


import com.alibaba.fastjson.JSONObject;
import com.schuanhe.plooks.domain.User;
import com.schuanhe.plooks.domain.form.LoginForm;
import com.schuanhe.plooks.service.User.UserService;
import com.schuanhe.plooks.utils.RedisCache;
import com.schuanhe.plooks.utils.ResponseResult;
import com.wf.captcha.SpecCaptcha;
import com.wf.captcha.base.Captcha;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;



@RestController
@RequestMapping("${base-url}/user") //读取配置文件中的base-url
public class UserController {

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private UserService userService;

    @GetMapping("/test")
    public ResponseResult<String> test(){
        return ResponseResult.success("测试成功");
    }

    //需要管理员权限
    @GetMapping("/admin")
    @PreAuthorize("hasAnyAuthority('system:test:list')")
    public ResponseResult<String> admin(){
        return ResponseResult.success("管理员权限");
    }


    /**
     * 登录
     * 从表单中获取用户名和密码
     * 登录错误计数器存储在Redis中，键名为"user:login:error:{username}"，过期时间设置为一定的时间窗口，比如5分钟
     */
    @PostMapping("/login")
    public ResponseResult<Map<String,String>> login(@RequestBody LoginForm loginForm){
        //判断表单是否符合要求
        if(loginForm == null || loginForm.getUser() == null){
            return ResponseResult.fail("表单不能为空");
        }
        //判断用户名和密码是否为空
        if(loginForm.getUser().getUsername() == null || loginForm.getUser().getPassword() == null){
            return ResponseResult.fail("用户名或密码不能为空");
        }
        //判断是否需要验证码
        Integer cache = 0;

        if (redisCache.hasKey("user:login:error:" + loginForm.getUser().getUsername())){
            try {
                cache = redisCache.getCacheObject("user:login:error:" + loginForm.getUser().getUsername());
            }catch (Exception e){
                return ResponseResult.error("服务器异常:类型转化");
            }
        }

        System.out.println(cache);
        if(cache >= 3){
            //需要验证码
            if (checkCaptcha(loginForm))
                return ResponseResult.fail(-1,"请输入正确的验证码");
        }

        //验证用户名和密码
        User user = loginForm.getUser();
        Map<String,String> token = userService.login(user);
        //如果账户密码错误则不会在这里来
        return ResponseResult.success(token);
    }

    /**
     * 邮箱登录验证码
     */
    @PostMapping("/login/email")
    public ResponseResult<Map<String,String>> loginEmail(@RequestBody LoginForm loginForm){
        //判断表单是否符合要求
        if(loginForm == null|| loginForm.getUser().getEmail() == null|| loginForm.getCode() == null)
            return ResponseResult.fail("表单不完整");
        //判断邮箱验证码
        if (checkEmailCaptcha(loginForm))
            return ResponseResult.fail(-1,"验证码错误或已过期");
        //验证码正确登录
        User user = loginForm.getUser();
        Map<String,String> token = userService.loginEmail(user.getEmail());
        return ResponseResult.success(token);
    }


    /**
     * 邮箱登录验证码
     */
    @PostMapping("/login/email/code")
    public ResponseResult<String> loginSendEmail(@RequestBody LoginForm loginForm){
        System.out.println("登录前时间"+new Date());
        //判断表单是否符合要求
        if(loginForm == null|| loginForm.getUser().getEmail() == null)
            return ResponseResult.fail("表单不完整");
        if (checkCaptcha(loginForm))
            return ResponseResult.fail(-1,"请输入正确的验证码");
        //判断邮箱和用户名是否已经被注册

        if (userService.isEmailExist(loginForm.getUser().getEmail())){
            //发送邮箱验证码
            userService.sendEmail(loginForm.getUser());
            System.out.println("登录后时间"+new Date());
            return ResponseResult.success("发送成功");
        }

        return ResponseResult.fail("邮箱未注册");
    }


    /**
     * 注册,需要验证码and邮箱验证码
     */
    @PostMapping("/register")
    public ResponseResult<String> register(@RequestBody LoginForm loginForm) {
        //判断表单是否符合要求
        if(loginForm == null || loginForm.getUser() == null)
            return ResponseResult.fail("表单不能为空");
        //判断用户名和密码是否为空
        if(loginForm.getUser().getUsername() == null || loginForm.getUser().getPassword() == null || loginForm.getUser().getEmail() == null)
            return ResponseResult.fail("表单不完整");
        //注册
        try {
            userService.isEmailAndNameExist(loginForm.getUser());
            userService.register(loginForm);
        }catch (RuntimeException e){
            return ResponseResult.fail(e.getMessage());
        }
        return ResponseResult.success("注册成功");
    }

    /**
     * 发送邮箱验证码
     */
    @PostMapping("/register/email")
    public ResponseResult<String> sendEmail(@RequestBody LoginForm loginForm){
        //判断表单是否符合要求
        if(loginForm == null || loginForm.getUser().getEmail() == null)
            return ResponseResult.fail("表单不完整");
        if (checkCaptcha(loginForm))
            return ResponseResult.fail(-1,"请输入正确的验证码");
        //判断邮箱是否已经被注册
        if (userService.isEmailExist(loginForm.getUser().getEmail()))
            return ResponseResult.fail("邮箱已被注册");

        //发送邮箱验证码
        userService.sendEmail(loginForm.getUser());
        return ResponseResult.success("发送成功");
    }

    /**
     * 刷新token
     */
    @GetMapping("/token/refresh")
    public ResponseResult<Map<String,String>> refreshToken(@RequestHeader("Authorization") String refreshToken){
        String token = userService.refreshToken(refreshToken);
        if(token == null){
            return ResponseResult.fail("刷新token失败");
        }
        Map<String,String> map = new HashMap<>();
        map.put("token",token);
        return ResponseResult.success(map);
    }

    /**
     * 验证码
     */
    @RequestMapping("/captchaImage/{uuid}")
    public void captcha(HttpServletResponse response, @PathVariable("uuid") String uuid) throws Exception {

        ServletOutputStream outputStream = response.getOutputStream();

        // 三个参数分别为宽、高、位数
        SpecCaptcha captcha = new SpecCaptcha(150, 40, 4);

        // 设置类型 数字和字母混合
        captcha.setCharType(Captcha.TYPE_ONLY_NUMBER);
        String result = captcha.text();

        // 随机生成uuid，存入redis，设置过期时间
        //String uuid = UUID.randomUUID().toString(); //使用前端的uuid

        // 将验证码和uuid存入redis
        redisCache.setCacheObject("user:captcha:" + uuid, result,60*5);
        // 将uuid和验证码返回给前端
        response.setContentType("image/png");
        captcha.out(outputStream);

    }

    /**
     * 用户获取个人信息
     */
    @GetMapping("/info")
    public ResponseResult<User> getUserInfo(@RequestHeader("Authorization") String token){
        User user = userService.getUserInfo(token);
        if(user == null){
            return ResponseResult.fail("获取用户信息失败");
        }
        return ResponseResult.success(user);
    }

    /**
     * 通过id获取用户部分信息
     */
    @GetMapping("/info/{id}")
    public ResponseResult<Map<String,User>> getUserInfoById(@PathVariable("id") Integer id){
        User user = userService.getUserInfoById(id);
        if(user == null){
            return ResponseResult.fail("获取用户信息失败");
        }

        Map<String,User> data = new HashMap<>();
        data.put("user",user);

        return ResponseResult.success(data);
    }

    /**
     * 通过昵称获取用户uid
     */
    @GetMapping("/uid/{nikeName}")
    public ResponseResult<Map<String,Integer>> getUserIdByUsername(@PathVariable("nikeName") String nikeName){
        Integer id = userService.getUserIdByNickName(nikeName);
        if(id == null){
            return ResponseResult.fail("获取用户id失败");
        }

        Map<String,Integer> map = new HashMap<>();
        map.put("uid",id);

        return ResponseResult.success(map);
    }



    /**
     * 邮箱修改密码验证
     */
    @PostMapping("/modify/email/code")
    public ResponseResult<String> modifyPasswordSendEmail(@RequestBody LoginForm loginForm){
        //判断表单是否符合要求
        if(loginForm == null|| loginForm.getUser().getEmail() == null)
            return ResponseResult.fail("表单不完整");
        if (checkCaptcha(loginForm))
            return ResponseResult.fail(-1,"请输入正确的验证码");
        if (userService.isEmailExist(loginForm.getUser().getEmail())){
            //发送邮箱验证码
            userService.sendEmail(loginForm.getUser());
            return ResponseResult.success("发送成功");
        }

        return ResponseResult.fail("邮箱未注册");
    }

    /**
     * 邮箱修改密码
     */
    @PostMapping("/modify/email")
    public ResponseResult<String> modifyPasswordByEmail(@RequestBody LoginForm loginForm){
        //判断表单是否符合要求
        if(loginForm == null || loginForm.getUser() == null)
            return ResponseResult.fail("表单不能为空");
        //判断用户名和密码是否为空
        if(loginForm.getUser().getPassword() == null || loginForm.getUser().getEmail() == null || loginForm.getCode() == null)
            return ResponseResult.fail("表单不完整");
        //修改密码
        try {
            userService.modifyPasswordByEmail(loginForm);
        }catch (RuntimeException e){
            return ResponseResult.fail(e.getMessage());
        }
        return ResponseResult.success("修改成功");
    }

    /**
     * 修改封面
     */
    @PutMapping("/cover")
    public ResponseResult<String> modifyCover(@RequestBody String url){
        if(url == null || url.equals(""))
            return ResponseResult.fail("表单不完整");
        try {
            JSONObject jsonObject = JSONObject.parseObject(url);
            String urlJson = jsonObject.getString("url");
            if(urlJson == null || urlJson.equals(""))
                urlJson = url;
            userService.modifyCover(urlJson);
        }catch (RuntimeException e){
            return ResponseResult.fail(e.getMessage());
        }
        return ResponseResult.success("修改成功");
    }

    /**
     * 用户修改个人信息
     */
    @PutMapping("/info")
    public ResponseResult<String> modifyUserInfo(@RequestBody User user){
        if(user == null)
            return ResponseResult.fail("表单不完整");
        try {
            userService.modifyUserInfo(user);
        }catch (RuntimeException e){
            return ResponseResult.fail(e.getMessage());
        }
        return ResponseResult.success("修改成功");
    }



    /**
     * 检查验证码
     * @param loginForm 表单
     * @return 是否有异常
     */
    private boolean checkCaptcha(LoginForm loginForm) {
        if (!StringUtils.hasLength(loginForm.getUuid()) || !StringUtils.hasLength(loginForm.getCode())) {
            return true;
        }
        String uuid = loginForm.getUuid();
        String code = loginForm.getCode();
        String redisCode = redisCache.getCacheObject("user:captcha:" + uuid);
        if (!code.equals(redisCode)) {
            return true;
        }
        //验证成功，删除验证码
        redisCache.deleteObject("user:captcha:" + uuid); // 测试阶段验证码不删除，可以重复使用
        return false;
    }

    /**
     * 邮箱验证码校验
     * @param loginForm 表单
     * @return 是否有异常
     */
    private boolean checkEmailCaptcha(LoginForm loginForm) {
        if (!StringUtils.hasLength(loginForm.getUser().getEmail()) || !StringUtils.hasLength(loginForm.getCode())) {
            return true;
        }
        String email = loginForm.getUser().getEmail();
        String code = loginForm.getCode();
        String redisCode = redisCache.getCacheObject("user:email:" + email);
        if (!code.equals(redisCode)) {
            return true;
        }
        //验证成功，删除验证码
        redisCache.deleteObject("user:email:" + email); // 测试阶段验证码不删除，可以重复使用
        return false;
    }


}
