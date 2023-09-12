<template>
    <div class="login-form">
        <!-- 账号登录 -->
        <n-tabs default-value="account" size="large" justify-content="space-evenly" @update:value="loginTypeChange">
            <n-tab-pane class="form-container" name="account" tab="账号登录">
                <n-form ref="accountFormRef" :rules="rules" :model="loginForm" label-placement="left" label-width="70">
                    <n-form-item label="账号" path="user.username">
                        <n-input placeholder="请输入账号" v-model:value="loginForm.user.username" />
                    </n-form-item>
                    <n-form-item label="密码" path="user.password">
                        <n-input placeholder="请输入密码)" v-model:value="loginForm.user.password" type="password">
                            <template #suffix>
                                <n-button type="primary" text @click="findPassword">登录</n-button>
                            </template>
                        </n-input>
                    </n-form-item>
                    <n-form-item label="验证码" path="code" v-if="loginCaptcha.show">
                        <n-input placeholder="请输入验证码" v-model:value="loginForm.code" />
                        <n-image :src="loginCaptcha.src" preview-disabled @click="openLoginCaptcha" />
                    </n-form-item>
                </n-form>
            </n-tab-pane>
            <!-- 邮箱登录 -->
            <n-tab-pane class="form-container" name="email" tab="邮箱登录">
                <n-form ref="emailFormRef" :rules="rules" :model="loginForm" label-placement="left" label-width="70">
                    <n-form-item label="邮箱" path="user.email">
                        <n-input placeholder="请输入邮箱" v-model:value="loginForm.user.email" />
                    </n-form-item>

                    <!-- 图像验证码 -->
                    <n-form-item label="验证码" v-show="!disabledSend">
                        <n-input placeholder="请输入图像验证码" v-model:value="emailCode" />
                        <n-image :src="eLoginCaptcha.src" preview-disabled @click="eOpenLoginCaptcha" />
                    </n-form-item>
                    
                    <n-form-item label="验证码" path="code">
                        <n-input placeholder="请输入邮箱验证码" v-model:value="loginForm.code" />
                        <n-button :disabled="disabledSend" @click="beforeSendCode">{{ sendBtnText }}</n-button>
                    </n-form-item>
                </n-form>
            </n-tab-pane>
        </n-tabs>
        <div class="login-btn">
            <n-button type="primary" @click="sendLoginRequest"> 登录 </n-button>
        </div>
    </div>
</template>

<script setup lang="ts">
import { reactive, ref } from 'vue';
import useSendCode from '@/hooks/send-code-hooks';
import type { UserLoginType,UserType,EmailCodeType } from "@plooks/apis";
import { loginAPI, emailLoginAPI, getUserInfoAPI, } from "@plooks/apis";
import { storageData, statusCode, globalConfig as config, createUuid } from '@plooks/utils';
import type { FormRules, FormInst } from 'naive-ui';
import { NTabs, NTabPane, NForm, NFormItem, NInput, NButton, useNotification, NImage } from 'naive-ui';
import { useRouter } from 'vue-router';

const emits = defineEmits(["changeForm", "success"]);

const router = useRouter();
// i18n

//通知组件
const notification = useNotification();

// 邮箱登录绑定
const emailCode = ref("");

// 登录图片验证码
const loginCaptcha = ref({
    "show": false,
    "uuid": "",
    "src": ""
});
const baseURL = config.domain ? `http${config.https ? 's' : ''}://${config.domain}` : '';
// 开启(刷新) 登录图片验证码
const openLoginCaptcha = () => {
    //拼接url
    loginCaptcha.value.show = true;
    const uuid = createUuid();
    //表单的uuid
    loginForm.uuid = uuid;
    loginCaptcha.value.src = baseURL + "/api/v1/user/captchaImage/" + uuid;

}

// 默认uuid
let euuid = createUuid();
// 登录图片验证码
const eLoginCaptcha = ref({
    "show": true, //是否显示验证码
    "uuid": euuid,
    "src": baseURL + "/api/v1/user/captchaImage/" + euuid
});

// 开启(刷新) 登录图片验证码
const eOpenLoginCaptcha = () => {
    euuid = createUuid();
    //表单的uuid
    eLoginCaptcha.value.uuid = euuid;
    loginForm.uuid = euuid;
    eLoginCaptcha.value.src = baseURL + "/api/v1/user/captchaImage/" + euuid;

}




//登录表单
const loginForm = reactive<UserLoginType>({
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

// 滑块验证通过

// const captchaSuccess = () => {
//     if (captchaUsers === "login") {
//         sendLoginRequest();
//     } else {
//         beforeSendCode();
//     }
// }

//发送验证码相关
const { disabledSend, sendBtnText, sendEmailCodeAsync } = useSendCode();
const beforeSendCode = () => {
    if (!loginForm.user.email) {
        return;
    }

    const emailCodeType:EmailCodeType ={
        email:loginForm.user.email,
        uuid: euuid,
        code: emailCode.value,
        scene : 2 //登录
    }

    //刷新验证码：
    eOpenLoginCaptcha()

    sendEmailCodeAsync(emailCodeType).then()
}

// 登录方式切换
const currentTabName = ref("account");
const loginTypeChange = (tabName: string) => {
    currentTabName.value = tabName;
}

//登录相关
const emailFormRef = ref<FormInst | null>(null);
const accountFormRef = ref<FormInst | null>(null);
//登录
const sendLoginRequest = () => {
    let currentRef: FormInst | null = null;
    switch (currentTabName.value) {
        case "account":
            emailLoginAPI
            currentRef = accountFormRef.value;
            break;
        case "email":
            currentRef = emailFormRef.value;
            break;
    }
    currentRef?.validate((err) => {
        if (!err) {
            const loginRequest = currentTabName.value === "account" ? loginAPI : emailLoginAPI;
            loginRequest(loginForm).then((res) => {
                switch (res.data.code) {
                    case statusCode.CAPTCHA_REQUIRED:
                        //弹出提示
                        notification.error({
                            title: res.data.message,
                            duration: 3000,
                        });
                        openLoginCaptcha();
                        break;
                    case statusCode.OK:
                        storageData.set("access_token", res.data.data.access_token, 5);
                        storageData.set("refresh_token", res.data.data.refresh_token, 14 * 24 * 60);
                        getUserInfoAPI().then((infoRes) => {
                            if (infoRes.data.code === statusCode.OK) {
                                const userInfo = infoRes.data.data;
                                storageData.set("user_info", infoRes.data.data, 14 * 24 * 60);
                                // 判断权限
                                if(userInfo.role === 0){
                                    notification.error({
                                        title: "暂无权限",
                                        duration: 3000,
                                    });
                                    return;
                                }
                            }
                        
                            emits("success");
                        })
                        break;
                    default:
                        notification.error({
                            title: res.data.message,
                            duration: 3000,
                        });
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

const findPassword = () => {
    let findPasswordUrl = router.resolve({ name: "FindPassword" });
    window.open(findPasswordUrl.href, '_blank');
}
</script>

<style lang="less" scoped>
.login-form {
    .form-container {
        box-sizing: border-box;
        padding: 40px 30px 0 30px;
    }

    .login-btn {
        display: flex;
        width: 100%;
        margin: 20px 30px 0;

        button {
            width: calc(100% - 60px);
        }
    }
}
</style>