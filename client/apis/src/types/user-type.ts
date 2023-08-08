// User 类
export interface UserType {
    id?: number
    username?: string
    email: string
    password?: string
}

export interface UserLoginType {
    user: UserType
    code?: string //验证码
    uuid?: string //uuid
}

export interface UserRegisterType {
    email: string
    password: string
    code: string //验证码
}

export interface UserInfoType {
    uid: number
    name: string
    avatar: string
    spacecover?: string
    email?: string
    gender?: number
    sign?: string
    birthday?: string
    created_at?: string
    role?: number
}

export interface ModifyUserInfoType {
    name: string,
    avatar: string,
    gender: number,
    sign: string,
    birthday: string
}

export interface ModifyPwdType {
    email: string
    password: string
    code: string //验证码
}

export interface AdminModifyUserInfoType {
    id: number,
    email: string,
    name: string,
    sign: string,
}
