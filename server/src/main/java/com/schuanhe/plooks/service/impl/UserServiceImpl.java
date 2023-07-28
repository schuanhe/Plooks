package com.schuanhe.plooks.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.schuanhe.plooks.domain.User;
import com.schuanhe.plooks.domain.model.UserDetailsImpl;
import com.schuanhe.plooks.service.UserService;
import com.schuanhe.plooks.mapper.UserMapper;
import com.schuanhe.plooks.utils.JwtUtil;
import com.schuanhe.plooks.utils.RedisCache;
import com.schuanhe.plooks.utils.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

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


    @Override
    public String login(User user) {

        // 创建 UsernamePasswordAuthenticationToken 对象，用于封装用户登录信息
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());

        System.out.println(authenticationToken);
        // 调用 authenticationManager 进行用户认证，返回认证后的结果
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);

        // 如果认证结果为空，抛出异常，表示登录失败
        if (Objects.isNull(authenticate)) {
            throw new RuntimeException("登录失败");
        }

        // 从认证结果中获取登录用户信息，其中包含用户的身份信息和权限等
        UserDetailsImpl loginUser = (UserDetailsImpl) authenticate.getPrincipal();

        // 获取登录用户的ID，并将其转换为字符串
        String userid = loginUser.getUser().getId().toString();

        // 根据用户ID生成 JWT（JSON Web Token）
        String jwt = JwtUtil.createJWT(userid);

        // 将登录用户信息存储到 Redis 缓存中，缓存的键为 "login:" + userid
        redisCache.setCacheObject("login:" + userid, loginUser);

        // 返回一个包含登录信息的 ResponseResult 对象，表示登录成功
        return jwt;
    }

    @Override
    public String createToken(User loginUser) {
        return null;
    }
}




