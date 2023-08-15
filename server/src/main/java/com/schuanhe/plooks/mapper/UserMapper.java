package com.schuanhe.plooks.mapper;

import com.schuanhe.plooks.domain.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.web.bind.annotation.RequestMapping;

/**
* @author ASUS
* @description 针对表【user(用户表)】的数据库操作Mapper
* @createDate 2023-07-27 08:14:23
* @Entity com.schuanhe.plooks.domain.User
*/
@Mapper
public interface UserMapper extends BaseMapper<User> {

    // 根据用户名查询用户
    User selectByUsername(String username);

    // 根据邮箱查询用户
    User selectByEmail(String email);

    // 通过邮箱修改密码
    Integer updatePasswordByEmail(User user);

    // 通过id修改用户背景图
    Integer updateCoverById(Integer id, String cover);

    Integer updateUserInfoById(User user);
}




