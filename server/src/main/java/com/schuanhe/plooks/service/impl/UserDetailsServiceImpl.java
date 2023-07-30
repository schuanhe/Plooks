package com.schuanhe.plooks.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

import com.schuanhe.plooks.domain.User;
import com.schuanhe.plooks.domain.model.UserDetailsImpl;
import com.schuanhe.plooks.mapper.UserMapper;
import com.schuanhe.plooks.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * 重写UserDetailsService接口的loadUserByUsername方法
 * 自定义用户认证逻辑
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisCache redisCache;


    @Override
// 根据用户名加载用户信息，用于Spring Security认证
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // 创建LambdaQueryWrapper对象，用于构建查询条件
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();

        // 添加查询条件：用户名等于指定的用户名
        wrapper.eq(User::getUsername, username);

        // 使用构建的查询条件查询数据库中的用户信息
        User user = userMapper.selectOne(wrapper);

        System.out.println(user);

        //// 如果用户为空，抛出异常，表示用户名或密码错误
        //if (Objects.isNull(user)) {
        //    // 写进redis中
        //        //登录失败，错误计数器加一
        //        //if (cache == null){
        //        //    redisCache.setCacheObject("user:login:error:" + username, String.valueOf(1),60*5);
        //        //}else {
        //            redisCache.increment("user:login:error:" + username,1);
        //        //}
        //    throw new RuntimeException("用户名或密码错误233");
        //}

        //TODO: 从数据库中查询用户拥有的权限列表
        List<String> list = new ArrayList<>(Arrays.asList("test", "admin"));

        // 从数据库中查询用户拥有的权限列表，替换上面的初始化列表
        //List<String> list = menuMapper.selectPermsByUserId(user.getId());

        // 创建并返回一个UserDetailsImpl对象，该对象包含从数据库中查询到的用户信息和权限列表
        return new UserDetailsImpl(user, list);
    }

}
