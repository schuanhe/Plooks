import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordEncoderTest {

    @Test
    public void testPasswordEncoder() {
        // 1. 设置测试环境（可以是您项目中的配置，这里简单使用BCryptPasswordEncoder作为示例）
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        // 2. 模拟用户注册或密码更新过程
        String plainPassword = "123456";

        // 3. 测试密码加密
        String encryptedPassword = passwordEncoder.encode(plainPassword);

        System.out.println(encryptedPassword);
        // 4. 断言密码匹配
        Assertions.assertTrue(passwordEncoder.matches(plainPassword, encryptedPassword));
    }
}
