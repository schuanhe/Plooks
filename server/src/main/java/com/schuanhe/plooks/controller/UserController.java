package com.schuanhe.plooks.controller;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.schuanhe.plooks.domain.User;
import com.schuanhe.plooks.domain.form.LoginForm;
import com.schuanhe.plooks.service.UserService;
import com.schuanhe.plooks.utils.RedisCache;
import com.schuanhe.plooks.utils.ResponseResult;
import com.wf.captcha.ArithmeticCaptcha;
import com.wf.captcha.SpecCaptcha;
import com.wf.captcha.base.Captcha;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private UserService userService;

    /**
     * 登录
     * 从表单中获取用户名和密码
     */
    @PostMapping("/login")
    public ResponseResult<String> login(@RequestBody LoginForm loginForm){
        System.out.println(loginForm);
        //验证验证码
        String uuid = loginForm.getUuid();
        String code = loginForm.getCode();
        String redisCode = redisCache.getCacheObject("user:captcha:" + uuid);
        if(!code.equals(redisCode)){
            return ResponseResult.fail("验证码错误");
        }
        //验证用户名和密码
        User user = loginForm.getUser();
        String token = userService.login(user);
        if(token == null){
            return ResponseResult.fail("用户名或密码错误");
        }
        //登录成功，生成token
        //String token = userService.createToken(loginUser);
        return ResponseResult.success(token);

    }

    /**
     * 验证码
     */
    @RequestMapping("/captchaImage")
    public void captcha(HttpServletResponse response) throws Exception{

        ServletOutputStream outputStream = response.getOutputStream();

        // 三个参数分别为宽、高、位数
        SpecCaptcha captcha = new SpecCaptcha(150, 40, 4);

        // 设置类型 数字和字母混合
        captcha.setCharType(Captcha.TYPE_ONLY_NUMBER);
        String result = captcha.text();

        // 随机生成uuid，存入redis，设置过期时间
        String uuid = UUID.randomUUID().toString();
        // 将验证码和uuid存入redis
        redisCache.setCacheObject("user:captcha:" + uuid, result,60*5);
        // 将uuid和验证码返回给前端
        response.setHeader("uuid",uuid); // 将uuid放入响应头
        response.setHeader("Access-Control-Expose-Headers","uuid"); // 告诉前端可以获取响应头的uuid
        response.setContentType("image/png");
        captcha.out(outputStream);

    }
}
