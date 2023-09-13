package com.schuanhe.plooks.service.User.impl;

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


    @Override
// 根据用户名加载用户信息，用于Spring Security认证
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // 创建LambdaQueryWrapper对象，用于构建查询条件
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();

        // 添加查询条件：用户名等于指定的用户名
        wrapper.eq(User::getUsername, username);

        // 使用构建的查询条件查询数据库中的用户信息
        User user = userMapper.selectOne(wrapper);

        //如果查询不到数据就通过抛出异常来给出提示
        if(Objects.isNull(user)){
            throw new RuntimeException("用户名或密码错误");
        }
        Integer role = user.getRole();
        List<String> list = new ArrayList<>();
        switch (role){
            case 3:
                // 如果用户角色为3，即超级管理员，就给用户添加所有权限
                list.add("role:superAdmin");
            case 2:
                list.add("role:admin"); // 如果用户角色为2，即管理员，就给用户添加admin权限
            case 1:
                // 如果用户角色为1，即为审核
                list.add("role:reviewer");
            case 0:
                // 如果用户角色为0，即为普通用户
                list.add("role:user");
            default:
                list.add("role:tourist"); // 如果用户角色为其他，即为游客
                break;
        }

        // 从数据库中查询用户拥有的权限列表，替换上面的初始化列表
        //List<String> list = menuMapper.selectPermsByUserId(user.getId());

        // 创建并返回一个UserDetailsImpl对象，该对象包含从数据库中查询到的用户信息和权限列表
        return new UserDetailsImpl(user, list);
    }

}
