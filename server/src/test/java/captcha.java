import com.schuanhe.plooks.domain.Comments;
import com.wf.captcha.ArithmeticCaptcha;
import com.wf.captcha.SpecCaptcha;
import com.wf.captcha.base.Captcha;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import javax.servlet.ServletOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class captcha {
    @Test
    public void captcha() {

        Comments.Comment comment = new Comments.Comment();
        List<Integer> at = new ArrayList<>();
        at.add(1);
        at.add(2);
        comment.setAt(at);
        System.out.println(comment);
        String atStr = comment.getAtStr();
        System.out.println(atStr);


        ////算术验证码 数字加减乘除. 建议2位运算就行:captcha.setLen(2);
        //ArithmeticCaptcha captcha = new ArithmeticCaptcha(120, 40);
        //// 获取运算的结果
        //captcha.setLen(2);
        //String result = captcha.text();
        //System.out.println(result);
        //// 随机生成uuid，存入redis，设置过期时间
        //String uuid = UUID.randomUUID().toString();


        // 算数验证码
        //Captcha captcha = new ArithmeticCaptcha();
        //// 文字验证码
        //// Captcha captcha2=new ChineseCaptcha();
        //// 生成的验证码
        //String text = captcha.text();
        //System.out.println("验证码 = " + text);
        //// 返回图片类型


        // 将验证码和uuid存入redis

        //// 三个参数分别为宽、高、位数
        //SpecCaptcha captcha = new SpecCaptcha(150, 40, 2);
        //
        //// 设置类型 数字和字母混合
        //captcha.setCharType(Captcha.TYPE_ONLY_NUMBER);
        //
        //
        //captcha.text();
        //System.out.println(captcha.text());

    }
}
