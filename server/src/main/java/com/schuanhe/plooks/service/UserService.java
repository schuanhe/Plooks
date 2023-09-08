package com.schuanhe.plooks.service;

import com.schuanhe.plooks.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.schuanhe.plooks.domain.form.LoginForm;

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

    Map<String, String> loginEmail(String email);

    /**
     * 刷新token
     * @param refreshToken 刷新token
     * @return 刷新结果
     */
    String refreshToken(String refreshToken);

    /**
     * 注册
     * @param loginForm 注册信息
     * @throws RuntimeException 注册失败抛出异常
     */
    void register(LoginForm loginForm) throws RuntimeException;

    /**
     * 验证用户名和邮箱是否存在
     * @param user 用户信息
     */
    void isEmailAndNameExist(User user) throws RuntimeException;

    /**
     * 验证邮箱是否存在
     */
    boolean isEmailExist(String email) throws RuntimeException;

    /**
     * 发送邮件
     * @param user 邮箱
     */
    void sendEmail(User user) throws RuntimeException;

    /**
     * 通过token获取用户信息
     * 加密敏感信息
     * @param token token
     * @return 用户信息
     */
    User getUserInfo(String token);


    /**
     * 去除敏感信息的用户信息
     * @param id 用户id
     * @return 用户信息
     */
    User getUserInfoById(Integer id);

    Integer getUserIdByNickName(String nickname);

    void modifyPasswordByEmail(LoginForm loginForm);

    void modifyCover( String spacecover);

    void modifyUserInfo(User user);

}
