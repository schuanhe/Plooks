import { ref } from 'vue';
import i18n from '@/locale';
import { useMessage } from 'naive-ui';
import { sendEmailCodeAPI } from '@plooks/apis';
import { statusCode } from '@plooks/utils';
import type { EmailCodeType } from '@plooks/apis';

export default function useSendCode() {

    const disabledSend = ref(false);//禁用发送按钮
    const sendBtnText = ref(i18n.global.t('code.sendCode'));//发送按钮文字
    const message = useMessage();//通知

    const sendEmailCodeAsync = async (emailCode: EmailCodeType) => {
        //禁用发送按钮
        disabledSend.value = true;
        try {
            const res = await sendEmailCodeAPI(emailCode);
            
            if (res.data.code === statusCode.OK) {
                message.success(i18n.global.t('code.sendSuccess'));
                //开启倒计时
                let count = 0;
                const tag = setInterval(() => {
                    if (++count >= 60) {
                        clearInterval(tag);
                        disabledSend.value = false;
                        sendBtnText.value = i18n.global.t('code.sendCode');
                        return;
                    }
                    sendBtnText.value = `${60 - count}${i18n.global.t('common.second')}`;
                }, 1000);
            } else {
                disabledSend.value = false;
                message.error(res.data.message);
            }
            return res.data.code;
        } catch {
            disabledSend.value = false;
            sendBtnText.value = i18n.global.t('code.sendCode');
            message.error(i18n.global.t('code.sendFail'));
        }
    }

    return {
        disabledSend,
        sendBtnText,
        sendEmailCodeAsync
    }
}

