import { ref } from 'vue';
import { useMessage } from 'naive-ui';
import { sendEmailCodeAPI } from '@plooks/apis';
import { statusCode } from '@plooks/utils';
import type { EmailCodeType } from '@plooks/apis';

export default function useSendCode() {

    const disabledSend = ref(false);//禁用发送按钮
    const sendBtnText = ref("发送验证码");//发送按钮文字
    const message = useMessage();//通知

    const sendEmailCodeAsync = async (emailCode: EmailCodeType) => {
        //禁用发送按钮
        disabledSend.value = true;
        try {
            const res = await sendEmailCodeAPI(emailCode);
            
            if (res.data.code === statusCode.OK) {
                message.success("邮箱验证码发送成功");
                //开启倒计时
                let count = 0;
                const tag = setInterval(() => {
                    if (++count >= 60) {
                        clearInterval(tag);
                        disabledSend.value = false;
                        sendBtnText.value = "发送验证码";
                        return;
                    }
                    sendBtnText.value = `${60 - count}秒后重发`;
                }, 1000);
            } else {
                disabledSend.value = false;
                message.error(res.data.message);
            }
            return res.data.code;
        } catch {
            disabledSend.value = false;
            sendBtnText.value = "发送验证码";
            message.error("发送验证码失败");
        }
    }

    return {
        disabledSend,
        sendBtnText,
        sendEmailCodeAsync
    }
}

