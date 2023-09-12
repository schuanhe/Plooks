package com.schuanhe.plooks.service.Admin.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.schuanhe.plooks.domain.User;
import com.schuanhe.plooks.mapper.UserMapper;
import com.schuanhe.plooks.service.Admin.UserAdminService;
import com.schuanhe.plooks.service.User.UserService;
import com.schuanhe.plooks.utils.RedisCache;
import com.schuanhe.plooks.utils.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;


@Service
public class UserAdminServiceImpl extends ServiceImpl<UserMapper, User> implements UserAdminService {

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private UserService userService;

    @Override
    public List<User> getUserList(Integer page, Integer size) {
        if (page <= 0){
            page = 1;
        }

        if (size <= 0){
            size = 10;
        }

        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();

        userQueryWrapper.isNull("deleted_at");

        userQueryWrapper.last("LIMIT " + (page - 1) * size + "," + size);

        return baseMapper.selectList(userQueryWrapper);
    }

    @Override
    public Long getUserCount() {
        return baseMapper.selectCount(null);
    }

    @Override
    public List<User> searchUser(String keyword, Integer page, Integer size) {
        if (page <= 0)
            page = 1;
        if (size <= 0)
            size = 10;
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.like("email",keyword).or().like("nickname",keyword);
        userQueryWrapper.isNull("deleted_at");
        userQueryWrapper.last("LIMIT " + (page - 1) * size + "," + size);
        return baseMapper.selectList(userQueryWrapper);
    }

    @Override
    public Long searchUserCount(String keyword) {
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.isNull("deleted_at");
        userQueryWrapper.like("email",keyword).or().like("nickname",keyword);
        return baseMapper.selectCount(userQueryWrapper);
    }

    @Override
    public void updateUser(User user) {
        User newUser = new User();
        newUser.setId(user.getId());
        if(user.getNickname() != null){
            newUser.setNickname(user.getNickname());
            redisCache.deleteObject("user:info:nickname:" + user.getId());
        }
        newUser.setEmail(user.getEmail());
        newUser.setSign(user.getSign());
        baseMapper.updateById(newUser);
        // 删除redis中的用户信息
        redisCache.deleteObject("user:info:user:" + user.getId());

    }

    @Override
    public void deleteUser(Integer uid) throws Exception {
        Integer userId = WebUtils.getUserId();
        if (userId != null && userId.equals(uid)){
            throw new Exception("不能删除自己");
        }

        User userInfoById = userService.getUserInfoById(uid);
        if (userInfoById == null)
            throw new Exception("用户不存在");
        System.out.println(userInfoById);
        if (userInfoById.getRole() == 3)
            throw new Exception("不能删除超级管理员");

        User user = new User();
        user.setId(uid);
        user.setDeletedAt(new Date());
        baseMapper.updateById(user);  // 删除用户
        // 删除redis中的用户信息
        redisCache.deleteObject("user:info:user:" + uid);
        redisCache.deleteObject("user:info:nickname:" + uid);
    }

    @Override
    public void updateRole(User user) throws Exception {
        Integer userId = WebUtils.getUserId();
        Integer uid = user.getId();
        Integer role = user.getRole();
        if (uid <= 0 && role < 0 ){
            throw new Exception("参数不正确");
        }
        if (userId != null && userId.equals(uid)){
            throw new Exception("不能修改自己");
        }
        User userInfo = userService.getUserInfoById(userId);
        if (userInfo != null && userInfo.getRole() <= role){
            throw new Exception("权限不足");
        }

        User newUser = new User();
        newUser.setId(uid);
        newUser.setRole(role);
        baseMapper.updateById(newUser);
        // 删除redis中的用户信息
        redisCache.deleteObject("user:info:user:" + uid);

    }
}
