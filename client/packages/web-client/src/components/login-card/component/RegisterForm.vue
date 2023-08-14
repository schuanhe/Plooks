<template>
    <div class="login-form">
        <!-- 邮箱注册 -->
        <n-tabs default-value="account" size="large" justify-content="space-evenly">
            <n-tab-pane class="form-container" name="account" :tab="t('register.emailRegister')">
                <n-form ref="emailFormRef" :show-feedback=false :rules="rules" :model="registerForm" label-placement="left" label-width="70">
                    <!-- 账号 -->
                    <n-form-item :label="t('common.account')" :show-feedback=false path="user.username" style="margin: 10px 0;">
                        <n-input :placeholder="t('input.account')" v-model:value="registerForm.user.username" />
                    </n-form-item>
                    <!-- 密码 -->
                    <n-form-item :label="t('common.pwd')" path="user.password" style="margin: 10px 0;">
                        <n-input :placeholder="t('input.pwd')" type="password" v-model:value="registerForm.user.password" />
                    </n-form-item>
                    <!-- 邮箱 -->
                    <n-form-item :label="t('common.email')" path="user.email" style="margin: 10px 0;">
                        <n-input :placeholder="t('input.email')" v-model:value="registerForm.user.email" />
                    </n-form-item>
                    <!-- 图像验证码 -->
                    <n-form-item :label="t('common.code')" v-show="!disabledSend" style="margin: 10px 0;">
                        <n-input :placeholder="t('input.code')" v-model:value="emailCode" />
                        <n-image :src="eLoginCaptcha.src" preview-disabled @click="eOpenLoginCaptcha" />
                    </n-form-item>

                    <!-- 邮箱验证码 -->
                    <n-form-item :label="t('common.ecode')" path="code" style="margin: 10px 0;">
                        <n-input :placeholder="t('input.ecode')" v-model:value="registerForm.code" />
                        <n-button :disabled="disabledSend" @click="beforeSendCode">{{ sendBtnText }}</n-button>
                    </n-form-item>
                </n-form>
            </n-tab-pane>
            <!-- 手机号注册 -->
            <!-- <n-tab-pane name="email" :tab="t('register.phoneRegister')" :disabled="true"></n-tab-pane> -->
        </n-tabs>
        <div class="login-btn">
            <n-button @click="emits('changeForm')">{{ t('register.backLogin') }}</n-button>
            <n-button type="primary" @click="sendRegisterRequest">{{ t('common.register') }}</n-button>
        </div>

    </div>
</template>

<script setup lang="ts">
import { reactive, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import useSendCode from '@/hooks/send-code-hooks';
import type { UserLoginType, UserType,EmailCodeType } from "@plooks/apis";
import { registerAPI } from "@plooks/apis";
import { isEmail, statusCode, createUuid,globalConfig as config } from '@plooks/utils';
import type { FormRules, FormInst } from 'naive-ui';
import { NTabs, NTabPane, NForm, NFormItem, NInput, NButton, useNotification, NImage } from 'naive-ui';
import { useLoginStore } from '@/stores/login-store';


const emits = defineEmits(["changeForm"]);

// i18n
const { t } = useI18n();

//通知组件
const notification = useNotification();

// 显示滑块验证
const showCaptcha = ref(false);

// 邮箱登录绑定
const emailCode = ref("");

const baseURL = config.domain ? `http${config.https ? 's' : ''}://${config.domain}` : '';
// 默认uuid
let euuid = createUuid();
// 登录图片验证码
const eLoginCaptcha = ref({
    "show": false, //是否显示验证码
    "uuid": euuid,
    "src": baseURL + "/api/v1/user/captchaImage/" + euuid
});

// 开启(刷新) 登录图片验证码
const eOpenLoginCaptcha = () => {
    euuid = createUuid();
    //表单的uuid
    eLoginCaptcha.value.show = true;
    eLoginCaptcha.value.uuid = euuid;
    registerForm.uuid = euuid;
    eLoginCaptcha.value.src = baseURL + "/api/v1/user/captchaImage/" + euuid;

}

//注册表单
const registerForm = reactive<UserLoginType>({
    user: reactive<UserType>({
        username: "",
        password: "",
        email: "",
    }),
    code: "",
    uuid: ""
});

//校验规则
const rules: FormRules = {
    user: {
        username: [
            { required: true, message: "请输入用户名", trigger: ['blur', 'input'] },
        ],
        password: [
            { required: true, message: "请输入密码", trigger: ['blur', 'input'] },
        ],
        email: [
            { required: true, message: "请输入邮箱", trigger: ['blur', 'input'] },
            { type: "email", message: "请输入正确的邮箱地址", trigger: ['blur', 'input'] },
        ]
    },
    code: { required: true, message: '请输入验证码', trigger: ['blur', 'input'] },
}

//发送验证码相关
const { disabledSend, sendBtnText, sendEmailCodeAsync } = useSendCode();
const beforeSendCode = () => {
        

    if (!registerForm.user.email || !isEmail(registerForm.user.email)) {
        notification.error({
                title: '请填写邮箱',
                duration: 3000,
            })
        return;
    }


    if (!emailCode.value) {
        notification.error({
                title: '请填写验证码',
                duration: 3000,
            })
        return;
    }

    const emailCodeType:EmailCodeType ={
        email:registerForm.user.email,
        uuid: euuid,
        code: emailCode.value,
        //注册
        scene : 1
    }

    //刷新验证码：
    eOpenLoginCaptcha()

    sendEmailCodeAsync(emailCodeType).then((res) => {
        if (res === statusCode.CAPTCHA_REQUIRED) {
            showCaptcha.value = true;
        }
    })
}

//登录相关
const emailFormRef = ref<FormInst | null>(null);
//登录
const sendRegisterRequest = () => {

    emailFormRef.value?.validate((err) => {
        if (!err) {
            registerAPI(registerForm).then((res) => {
                if (res.data.code === statusCode.OK) {
                    //写入用户数据到路由
                    useLoginStore().setLoginData(registerForm.user)
                    //跳转到登录页面
                    emits('changeForm') 
                    notification.success({
                        title: '注册成功',
                        duration: 3000,
                    })

                

                } else{
                    notification.error({
                        title: res.data.message,
                        duration: 3000,
                    })
                }
            });
        } else {
            notification.error({
                title: '请检查输入的数据',
                duration: 3000,
            })
        }
    });
}
</script>

<style lang="less" scoped>
.login-form {
    .form-container {
        box-sizing: border-box;
        padding: 0px 30px 0 30px;
    }

    .login-btn {
        display: flex;
        justify-content: space-between;
        margin: 6px 30px 0;

        button {
            width: 160px;
        }
    }
}
</style>