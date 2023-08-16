import request from '../request';
import type { AdminModifyUserInfoType, ModifyUserInfoType, UserLoginType } from '../types/user-type'

// 登录
export const loginAPI = (login: UserLoginType) => {
    return request.post('v1/user/login', login);
}

// 邮箱登录
export const emailLoginAPI = (login: UserLoginType) => {
    return request.post('v1/user/login/email', login);
}

// 注册
export const registerAPI = (register: UserLoginType) => {
    return request.post('v1/user/register', register);
}

// 获取用户信息
export const getUserInfoAPI = () => {
    return request.get('v1/user/info');
}

// 修改用户信息
export const modifyUserInfoAPI = (modify: ModifyUserInfoType) => {
    return request.put('v1/user/info', modify);
}

// 修改用户封面图
export const modifySpaceCoverAPI = (url: string) => {
    return request.put('v1/user/cover', { url });
}

//通过用户ID获取用户信息
export const getOtherUserInfoAPI = (uid: number) => {
    return request.get(`v1/user/info/other?uid=${uid}`);
}

//通过用户名获取用户ID
export const getUserIdAPI = (name: string) => {
    return request.get(`v1/user/uid?name=${name}`);
}


// 重置密码
export const mpdifyPwdAPI = (modifyPwd: UserLoginType) => {
    return request.post('v1/user/modify/email', modifyPwd);
}

// 管理员获取用户列表
export const adminGetUserListAPI = (page: number, pageSize: number) => {
    return request.get(`v1/user/manage/list?page=${page}&page_size=${pageSize}`)
}

// 管理员搜索用户
export const adminSearchUserAPI = (keyword: string, page: number, pageSize: number) => {
    return request.get(`v1/user/manage/search?keyword=${keyword}&page=${page}&page_size=${pageSize}`)
}

// 管理员修改用户信息
export const adminModifyUserInfoAPI = (modify: AdminModifyUserInfoType) => {
    return request.post('v1/user/manage/modify', modify);
}

// 管理员修改用户角色
export const adminModifyUserRoleAPI = (id: number, role: number) => {
    return request.post('v1/user/manage/role/modify', { id, role });
}

// 管理员删除用户
export const adminDeleteUserAPI = (id: number) => {
    return request.post("v1/user/manage/delete", { id })
}