import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.Date;


@SpringBootTest(classes = com.schuanhe.plooks.Application.class)
public class EmailTest {
    @Autowired
    private JavaMailSender javaMailSender;

    // 获取yml配置的发送者邮箱
    @Value("${spring.mail.username}")
    private String mainUserName;


    /**
     * 普通邮件发送
     */
    @Test
    public void sendSimpleMail() {
        // 构建一个邮件对象
        SimpleMailMessage message = new SimpleMailMessage();
        // 设置邮件主题
        message.setSubject("测试邮件");
        // 设置邮件发送者，昵称+<邮箱地址>
        message.setFrom("幻鹤工作室"+'<'+mainUserName+'>');
        // 设置邮件接收者，可以有多个接收者，多个接受者参数需要数组形式
        message.setTo("schuanhe@qq.com");
        //// 设置邮件抄送人，可以有多个抄送人
        //message.setCc("12****32*qq.com");
        //// 设置隐秘抄送人，可以有多个
        //message.setBcc("7******9@qq.com");
        // 设置邮件发送日期
        message.setSentDate(new Date());
        // 设置邮件的正文
        message.setText("hello,do you one more");
        // 发送邮件
        javaMailSender.send(message);
    }

}
