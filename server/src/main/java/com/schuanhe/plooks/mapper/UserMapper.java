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

}




