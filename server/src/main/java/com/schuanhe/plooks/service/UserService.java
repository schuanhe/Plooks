package com.schuanhe.plooks.service;

import com.schuanhe.plooks.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author ASUS
* @description 针对表【user(用户表)】的数据库操作Service
* @createDate 2023-07-27 08:14:23
*/
public interface UserService extends IService<User> {

    /**
     * 登录
     * @param user 用户信息
     * @return 登录成功返回，失败返回null
     */
    String login(User user);

    /**
     * 生成token
     * @param loginUser 登录成功的用户信息
     * @return token
     */
    String createToken(User loginUser);
}
