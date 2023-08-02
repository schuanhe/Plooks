package com.schuanhe.plooks.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.schuanhe.plooks.domain.User;
import com.schuanhe.plooks.domain.model.UserDetailsImpl;
import com.schuanhe.plooks.service.UserService;
import com.schuanhe.plooks.mapper.UserMapper;
import com.schuanhe.plooks.utils.JwtUtil;
import com.schuanhe.plooks.utils.RedisCache;
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
        jwt.put("accessToken", accessToken);
        jwt.put("refreshToken", refreshToken);

        // 将 accessToken放到redis中
        redisCache.setCacheObject("user:token:" + accessToken, userid, 60 * 60);

        // 将登录用户信息存储到 Redis 缓存中，缓存的键为 "login:" + userid
        redisCache.setCacheObject("login:" + userid, loginUser);

        // 返回一个包含登录信息的 ResponseResult 对象，表示登录成功
        return jwt;
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
    public void register(User user){
        // 判断用户名是否已经存在

        User user1 = baseMapper.selectByUsername(user.getUsername());
        if (Objects.nonNull(user1)) {
            throw new RuntimeException("用户名已存在");
        }

        // 判断邮箱是否已经存在
        User user2 = baseMapper.selectByEmail(user.getEmail());
        if (Objects.nonNull(user2)) {
            throw new RuntimeException("邮箱已存在");
        }

        // 处理密码(加密)
        String password = passwordEncoder.encode(user.getPassword());
        user.setPassword(password);
        // 设置用户注册时间
        user.setCreatedAt(new Date());
        user.setUpdatedAt(new Date());


        // 将用户信息插入到数据库中
        baseMapper.insert(user);

    }

}




