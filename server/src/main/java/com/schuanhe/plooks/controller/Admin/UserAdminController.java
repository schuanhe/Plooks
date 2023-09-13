package com.schuanhe.plooks.controller.Admin;


import com.schuanhe.plooks.domain.User;
import com.schuanhe.plooks.service.Admin.UserAdminService;
import com.schuanhe.plooks.utils.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("${base-url}/user/admin")
public class UserAdminController {

    @Autowired
    private UserAdminService userAdminService;


    /**
     * 获取用户列表
     */
     @GetMapping("/{size}/{page}")
     public ResponseResult<?> getUserList(@PathVariable Integer page, @PathVariable Integer size) {

         List<User> users = userAdminService.getUserList(page,size);

         Long userCount = userAdminService.getUserCount();

            // 返回用户列表
         Map<String, Object> data = new HashMap<>();
         data.put("total", userCount);
         data.put("users", users);
         return ResponseResult.success(data);
     }

    /**
     * 搜索用户
     */
    @GetMapping("/search/{keyword}/{size}/{page}")
    public ResponseResult<?> searchUser(@PathVariable String keyword, @PathVariable Integer page, @PathVariable Integer size) {

        List<User> users = userAdminService.searchUser(keyword,page,size);

        Long userCount = userAdminService.searchUserCount(keyword);

        // 返回用户列表
        Map<String, Object> data = new HashMap<>();
        data.put("total", userCount);
        data.put("users", users);
        return ResponseResult.success(data);
    }

    /**
     * 修改用户信息
     */
    @PutMapping
    public ResponseResult<?> updateUser(@RequestBody User user) {
        userAdminService.updateUser(user);
        return ResponseResult.success();
    }

    /**
     * 修改用户角色
     */
    @PutMapping("/role")
    public ResponseResult<?> updateRole(@RequestBody User user){
        try {
            userAdminService.updateRole(user);
        }catch (Exception e){
            return ResponseResult.error(e.getMessage());
        }
        return ResponseResult.success();
    }

    /**
     * 删除用户
     */
    @PreAuthorize("hasAnyAuthority('')")
    @DeleteMapping("/{uid}")
    public ResponseResult<?> deleteUser(@PathVariable Integer uid) {
        try {
            userAdminService.deleteUser(uid);
        } catch (Exception e) {
            return ResponseResult.error(e.getMessage());
        }
        return ResponseResult.success();
    }





}
