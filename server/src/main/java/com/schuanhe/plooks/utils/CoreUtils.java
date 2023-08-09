package com.schuanhe.plooks.utils;

import com.schuanhe.plooks.domain.User;

/**
 * 基础的通用操作
 */
public class CoreUtils {

    /**
     * 对用户信息进行脱敏处理
     */
    public static User desensitization(User user) {
        if (user == null) {
            return null;
        }
        user.setPassword(null);
        user.setStatus(null);
        //对邮箱脱敏处理
        String email = user.getEmail();
        if (email != null) {
            int index = email.indexOf("@");
            if (index > 0) {
                String prefix = email.substring(0, index);
                String suffix = email.substring(index);
                if (prefix.length() > 2) {
                    user.setEmail(prefix.substring(0, 2) + "****" + suffix);
                } else {
                    user.setEmail(prefix + "****" + suffix);
                }
            }
        }
        return user;
    }




}
