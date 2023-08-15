package com.schuanhe.plooks.utils;

import com.schuanhe.plooks.domain.User;

import java.util.Random;

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

    /**
     * 去除用户关键数据
     */
    public static User removeSensitiveInfo(User user) {
        if (user == null) {
            return null;
        }
        user.setPassword(null);
        user.setStatus(null);
        user.setEmail(null);
        user.setBirthday(null);
        user.setClientIp(null);
        user.setCreatedAt(null);
        user.setUpdatedAt(null);
        user.setRole(null);
        return user;
    }


    public static String getRandomString(int i) {
        String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder sb = new StringBuilder();
        for (int j = 0; j < i; j++) {
            char ch = str.charAt(new Random().nextInt(str.length()));
            sb.append(ch);
        }
        return sb.toString();
    }


}
