import request from '../request';
import type { EmailCodeType,UserLoginType } from '../types/user-type';

// 发送验证码
export const sendEmailCodeAPI = (email: EmailCodeType) => {
    // 将EmailCodeType，转化为UserLoginType
    const userLogin: UserLoginType = {
        user: {
            email: email.email,
            // 有userneme的场景，才传username
            username: email.username ? email.username : undefined
        },
        code: email.code,
        uuid: email.uuid
    }

    switch (email.scene) {
        case 1:
            return request.post("v1/user/register/email", userLogin); // 注册验证码
        case 2:
            return request.post("v1/user/login/email/code", userLogin); // 登录验证码
        case 3:
            return request.post("v1/user/modify/email/code", userLogin); // 修改密码
        default:
            return request.post("v1/user/register/email", userLogin); // 默认为注册
    }
}
