package com.schuanhe.plooks.service;

import com.schuanhe.plooks.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
* @author ASUS
* @description 针对表【user(用户表)】的数据库操作Service
* @createDate 2023-07-27 08:14:23
*/
public interface UserService extends IService<User> {

    /**
     * 登录
     * @param user 用户信息
     * @return 登录结果
     */
    Map<String,String> login(User user);

    /**
     * 刷新token
     * @param refreshToken 刷新token
     * @return 刷新结果
     */
    String refreshToken(String refreshToken);

    /**
     * 注册
     * @param user 用户信息
     * @throws RuntimeException 注册失败抛出异常
     */
    void register(User user) throws RuntimeException;

}
