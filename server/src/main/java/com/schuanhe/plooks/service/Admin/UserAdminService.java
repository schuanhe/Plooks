package com.schuanhe.plooks.service.Admin;

import com.schuanhe.plooks.domain.User;

import java.util.List;

public interface UserAdminService {
    List<User> getUserList(Integer page, Integer size);

    Long getUserCount();

    List<User> searchUser(String keyword, Integer page, Integer size);

    Long searchUserCount(String keyword);

    void updateUser(User user);

    void deleteUser(Integer uid) throws Exception;

    void updateRole(User user) throws Exception;
}
