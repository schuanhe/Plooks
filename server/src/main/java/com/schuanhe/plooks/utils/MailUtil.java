package com.schuanhe.plooks.utils;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class MailUtil {

    @Autowired
    private JavaMailSender javaMailSender;


    // 获取yml配置的发送者邮箱
    @Value("${spring.mail.username}")
    private String mainUserName;

    @Value("${spring.mail.nickname}")
    private String nickname;

    /**
     * 普通邮件发送
     */
    public void sendSimpleMail(String to, String subject, String content) {
        // 构建一个邮件对象
        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(to);
        message.setSubject(subject);
        message.setText(content);

        message.setFrom(nickname+'<'+mainUserName+'>');
        message.setSentDate(new Date());
        // 发送邮件
        javaMailSender.send(message);
    }
}
