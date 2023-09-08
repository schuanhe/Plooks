package com.schuanhe.plooks.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.schuanhe.plooks.domain.User;
import com.schuanhe.plooks.domain.form.LoginForm;
import com.schuanhe.plooks.domain.model.UserDetailsImpl;
import com.schuanhe.plooks.service.UserService;
import com.schuanhe.plooks.mapper.UserMapper;
import com.schuanhe.plooks.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
* @author ASUS
* @description 针对表【user(用户表)】的数据库操作Service实现
* @createDate 2023-07-27 08:14:23
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private MailUtil mailUtil;


    @Override
    public Map<String, String> login(User user) {

        // 创建 UsernamePasswordAuthenticationToken 对象，用于封装用户登录信息
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());

        // 调用 authenticationManager 进行用户认证，返回认证后的结果,如果认证失败则会自动在UserDetailsServiceImpl中抛出异常
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);

        // 从认证结果中获取登录用户信息，其中包含用户的身份信息和权限等
        UserDetailsImpl loginUser = (UserDetailsImpl) authenticate.getPrincipal();

        // 获取登录用户的ID，并将其转换为字符串
        String userid = loginUser.getUser().getId().toString();

        // 根据用户ID生成 JWT（双token） access token1小时过期 refresh token 14天过期
        String accessToken = JwtUtil.createJWT(userid);
        String refreshToken = JwtUtil.createJWT(userid,  1000L * 60 * 60 * 24 * 14);

        // 将 JWT 以键值对的形式存储到 Map 中
        Map<String, String> jwt = new HashMap<>();
        jwt.put("access_token", accessToken);
        jwt.put("refresh_token", refreshToken);

        // 将 accessToken放到redis中
        redisCache.setCacheObject("user:token:" + accessToken, userid, 60 * 5);

        // 将登录用户信息存储到 Redis 缓存中，缓存的键为 "login:" + userid
        redisCache.setCacheObject("user:info:" + userid, loginUser);
        // 用户名对应的用户ID
        redisCache.setCacheObject("user:info:username" + user.getUsername(), userid);

        //删除错误次数
        redisCache.deleteObject("user:login:error:" + user.getUsername());

        // 返回一个包含登录信息的 ResponseResult 对象，表示登录成功
        return jwt;
    }

    /**
     * 邮箱登录
     * @param email 提供邮箱即可
     * @return jwt
     */
    @Override
    public Map<String, String> loginEmail(String email) {
        return login(baseMapper.selectByEmail(email));
    }

    @Override
    public String refreshToken(String refreshToken) {
        // 从 refreshToken 中获取用户ID
        String userid;
        try {
            userid = JwtUtil.getUserid(refreshToken);
        } catch (Exception e) {
            return null;
        }

        if (userid == null) {
            return null;
        }

        // 根据用户ID生成新的 accessToken
        String accessToken = JwtUtil.createJWT(userid);

        // 将 accessToken 放到 redis 中
        redisCache.setCacheObject("user:token:" + accessToken, userid, 60 * 60);

        // 返回包含新的 accessToken 和 refreshToken 的 Map
        return accessToken;
    }

    @Override
    public void register(LoginForm loginForm){

        User user = loginForm.getUser();
        // 邮箱验证码校验
        String code = redisCache.getCacheObject("user:email:" + user.getEmail());
        if (code==null || !code.equals(loginForm.getCode())) {
            throw new RuntimeException("验证码错误");
        }


        isEmailAndNameExist(user);

        // 处理密码(加密)
        String password = passwordEncoder.encode(user.getPassword());
        user.setPassword(password);
        // 设置用户注册时间
        user.setCreatedAt(new Date());
        user.setUpdatedAt(new Date());

        // 设置默认生日1970-01-01
        user.setBirthday(new Date(0));
        // 设置默认昵称
        user.setNickname("用户" + CoreUtils.getRandomString(4) + user.getId().toString());

        // 将用户信息插入到数据库中
        int insert = baseMapper.insert(user);
        if (insert != 1) {
            throw new RuntimeException("注册失败");
        }
        // 删除验证码
        redisCache.deleteObject("user:email:" + user.getEmail());

    }

    @Override
    public void isEmailAndNameExist(User user) {
        User user1 = baseMapper.selectByUsername(user.getUsername());
        if (Objects.nonNull(user1)) {
            throw new RuntimeException("用户名已存在");
        }

        // 判断邮箱是否已经存在
        User user2 = baseMapper.selectByEmail(user.getEmail());
        if (Objects.nonNull(user2)) {
            throw new RuntimeException("邮箱已存在");
        }
    }

    @Override
    public boolean isEmailExist(String email) throws RuntimeException {
        User user = baseMapper.selectByEmail(email);
        return Objects.nonNull(user);
    }

    @Override
    public void sendEmail(User user) {
        //生成4位数验证码
        String code = String.valueOf((int)((Math.random()*9+1)*1000));
        // 发送邮件
        System.out.println("邮件发送验证码："+code);
        mailUtil.sendSimpleMail(user.getEmail(),"Plooks验证码","你的验证码是："+ code +"，有效时间为5分钟(如非本人操作，请忽略此邮件)");
        // 将验证码存入redis中
        redisCache.setCacheObject("user:email:" + user.getEmail(), code, 60 * 5);
    }

    @Override
    public User getUserInfo(String token) {
        try {
            String userid = JwtUtil.getUserid(token);
            User user = redisCache.getCacheObject("user:info:user:" + userid);
            // 如果redis中没有用户信息，则从数据库中获取用户信息
            if (user == null) {
                user = baseMapper.selectById(userid);
                // 将用户信息存储到 Redis 缓存中
                redisCache.setCacheObject("user:info:user:" + userid, user);
            }
            return CoreUtils.desensitization(user); // 脱敏处理
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public User getUserInfoById(Integer id) {
        // 先从缓存中获取用户信息
        User user = redisCache.getCacheObject("user:info:user:" + id);
        // 如果缓存中没有用户信息，则从数据库中获取用户信息
        if (user == null) {
            user = baseMapper.selectById(id);
            // 将用户信息存储到 Redis 缓存中
            redisCache.setCacheObject("user:info:user:" + id, user);
        }
        return CoreUtils.removeSensitiveInfo(user); // 去除敏感信息
    }

    @Override
    public Integer getUserIdByNickName(String nickname) {
        // 先从缓存中获取用户信息
        Integer userid = redisCache.getCacheObject("user:info:nickname" + nickname);
        // 如果缓存中没有用户信息，则从数据库中获取用户信息
        if (userid == null) {

            User user = baseMapper.selectOne(new QueryWrapper<User>().eq("nickname", nickname).isNull("deleted_at"));
            // 将用户信息存储到 Redis 缓存中
            redisCache.setCacheObject("user:info:nickname" + nickname, user.getId());
            return user.getId();
        } else {
            return userid;
        }
    }


    @Override
    public void modifyPasswordByEmail(LoginForm loginForm) {
        // 邮箱验证码校验
        String code = redisCache.getCacheObject("user:email:" + loginForm.getUser().getEmail());
        if (code==null || !code.equals(loginForm.getCode())) {
            throw new RuntimeException("验证码错误");
        }

        // 处理密码(加密)
        String password = passwordEncoder.encode(loginForm.getUser().getPassword());
        loginForm.getUser().setPassword(password);

        // 将用户信息插入到数据库中
        int update = baseMapper.updatePasswordByEmail(loginForm.getUser());
        if (update != 1) {
            throw new RuntimeException("修改失败");
        }
        // 删除验证码
        redisCache.deleteObject("user:email:" + loginForm.getUser().getEmail());
    }

    @Override
    public void modifyCover(String spacecover) {
        Integer userid = WebUtils.getUserId();
        if (userid == null){
            throw new RuntimeException("没登录");
        }

        int update = baseMapper.updateCoverById(userid, spacecover);
        if (update != 1) {
            throw new RuntimeException("修改失败");
        }
        updateRedisCache(userid);
    }


    @Override
    public void modifyUserInfo(User user) {
        Integer userid = WebUtils.getUserId();
        if (userid == null){
            throw new RuntimeException("没登录");
        }
        // 查询新昵称是否存在
        if (user.getNickname()!=null){
            Integer userIdByNickName = this.getUserIdByNickName(user.getNickname());
            if (userIdByNickName!=null && !userIdByNickName.equals(userid)){
                throw new RuntimeException("该昵称已经存在存在");
            }
        }

        user.setId(userid);
        int update = baseMapper.updateUserInfoById(user);
        if (update != 1) {
            throw new RuntimeException("修改失败");
        }
        updateRedisCache(userid);
    }

    // 更新Redis缓存
    private void updateRedisCache(Integer userid) {
        // 老旧的用户信息
        UserDetailsImpl userDetailsOld = redisCache.getCacheObject("user:info:" + userid);
        // 删除缓存
        redisCache.deleteObject("user:info:" + userid);
        userDetailsOld.setUser(baseMapper.selectById(userid));
        redisCache.setCacheObject("user:info:" + userid, userDetailsOld);
    }


    // 根据token获取用户id
    private String getIdByToken(String token) throws RuntimeException{
        String userid;
        try {
            // 获取用户ID（先看看缓存）
            userid = redisCache.getCacheObject("user:token:" + token);
            if (userid == null) {
                userid = JwtUtil.getUserid(token);
                if (userid == null) {
                    throw new RuntimeException("token为空");
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("token解析错误");
        }
        return userid;
    }

}




