<template>
    <div v-title :data-title="`找回密码-${config.title}`">
        <header-bar></header-bar>
        <div class="find-password">
            <steps class="steps" :current="current" :title-list="['填写账号', '重置密码', '操作成功']"></steps>
            <div class="form" v-if="current === 1">
                <n-form :rules="rules" :model="modifyForm" label-width="auto">
                    <n-form-item label="邮箱" path="email">
                        <n-input placeholder="请输入绑定的邮箱" v-model:value="modifyForm.email"></n-input>
                    </n-form-item>
                    <!-- 图像验证码 -->
                    <n-form-item label="验证码" path="code">
                        <n-input placeholder="请输入验证码" v-model:value="modifyForm.code" />
                        <n-image :src="captcha.src" preview-disabled @click="OpenCaptcha" />
                    </n-form-item>
                    <n-button class="submit" type="primary" @click="checkUser">验证</n-button>
                </n-form>
            </div>
            <div v-else-if="current === 2">
                <n-form ref="formRef" :rules="rules" :model="modifyForm" label-width="auto">
                    <n-form-item label="邮箱">
                        <span>{{ desensiEmail(modifyForm.email) }}</span>

                    </n-form-item>
                    <n-form-item label="密码" path="password">
                        <n-input placeholder="请输入密码" type="password" v-model:value="modifyForm.password" />
                    </n-form-item>
                    <n-form-item label="确认密码" path="reenteredPassword">
                        <n-input placeholder="请输入确认密码" type="password" v-model:value="modifyForm.reenteredPassword" />
                    </n-form-item>
                    <n-form-item label="验证码" path="code">
                        <n-input placeholder="请输入验证码" v-model:value="modifyForm.code" />
                    </n-form-item>
                    <n-button class="submit" type="primary" @click="submitForm">保存</n-button>
                </n-form>
            </div>
            <div v-else-if="current === 3">
                <n-result status="success" title="成功" description="重置密码成功">
                    <template #footer>
                        <n-button @click="goHome">返回首页</n-button>
                    </template>
                </n-result>
            </div>
        </div>
    </div>
</template>

<script setup lang="ts">
import { reactive, ref } from "vue";
import HeaderBar from "@/components/header-bar/Index.vue"
import { globalConfig as config, statusCode,createUuid } from '@plooks/utils';
import type { FormInst, FormItemRule, FormRules } from "naive-ui";
import { NResult, NInput, NButton, NForm, NFormItem, useMessage, NImage } from "naive-ui";
import { mpdifyPwdAPI,sendEmailCodeAPI } from "@plooks/apis";
import type { UserLoginType,UserType,EmailCodeType } from "@plooks/apis";
import { useRouter } from "vue-router";

import Steps from "@/components/steps/Index.vue"

const message = useMessage();

const current = ref(1);


const modifyForm = reactive({
    email: "",
    password: "",
    reenteredPassword: "",
    code: "",
    uuid:""
})


// 提交表单



// 验证码相关
const baseURL = config.domain ? `http${config.https ? 's' : ''}://${config.domain}` : '';
// 默认uuid
let euuid = createUuid();
// 登录图片验证码
const captcha = ref({
    "uuid": euuid,
    "src": baseURL + "/api/v1/user/captchaImage/" + euuid
});

// 开启(刷新) 登录图片验证码
const OpenCaptcha = () => {
    euuid = createUuid();
    captcha.value.uuid = euuid;
    captcha.value.src = baseURL + "/api/v1/user/captchaImage/" + euuid;
}



const validatePasswordSame = (rule: FormItemRule, value: string): boolean => {
    return value === modifyForm.password
}

const rules: FormRules = {

    email: [
        { required: true, message: "请输入邮箱", trigger: ['blur', 'input'] },
        { type: "email", message: "请输入正确的邮箱地址", trigger: ['blur', 'input'] },
    ],
    password: [{ required: true, message: '请输入密码', trigger: ['input', 'blur'] }],
    reenteredPassword: [
        { required: true, message: '请再次输入密码', trigger: ['input', 'blur'] },
        { validator: validatePasswordSame, message: '两次密码输入不一致', trigger: ['blur', 'password-input'] }
    ],
    code: { required: true, message: '请输入验证码', trigger: ['blur', 'input'] },
}


const checkUser = () => {
    if (!modifyForm.email) {
        message.error("邮箱不能为空");
        return;
    }
    if (!modifyForm.code) {
        message.error("验证码不能为空");
        return;
    }

    const emailCode: EmailCodeType = {
        email: modifyForm.email,
        code: modifyForm.code,
        uuid: captcha.value.uuid,
        scene: 3
    }

    sendEmailCodeAPI(emailCode).then((res) => {
        if (res.data.code === statusCode.OK) {
            current.value = 2;
            // 清空验证码
            modifyForm.code = "";
            message.success("发送验证码成功，注意查收");
        } else {
            message.error(res.data.message);
        }        
    })
}

// 处理显示的邮箱
const desensiEmail = (email: string) => {
    return email.replace(/(.{0,3}).*@(.*)/, "$1***@$2")
}


// 提交表单
const formRef = ref<FormInst | null>(null);
const submitForm = () => {
    formRef.value?.validate((errors) => {
        if (!errors) {

            const modify: UserLoginType = reactive({
                user: reactive<UserType>({
                    password: modifyForm.password,
                    email: modifyForm.email,
                }),
                code: modifyForm.code,
            })

            mpdifyPwdAPI(modify).then((res) => {
                if (res.data.code === statusCode.OK) {
                    current.value = 3;
                    message.success("修改成功");
                } else {
                    message.error(res.data.message);
                }
            })
        } else {
            message.error("请检查输入的数据");
        }
    })
}

const router = useRouter();
const goHome = () => {
    router.push({ name: "Home" });
}

</script>

<style lang="less" scoped>
.find-password {
    width: 800px;
    margin: 30px auto;

    .steps {
        width: 100%;
    }

    .form {
        width: 520px;
        margin-left: 160px;

        .submit {
            width: 100%;
            margin-top: 30px;
        }
    }
}
</style>