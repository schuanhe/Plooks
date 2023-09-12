// User 类
export interface UserType {
    id?: number
    username?: string
    email?: string
    password?: string
}

export interface UserLoginType {
    user: UserType
    code?: string //验证码
    uuid?: string //uuid
}


export interface UserInfoType {
    uid: number
    nickname: string
    avatar: string
    spaceCover?: string
    email?: string
    gender?: number
    sign?: string
    birthday?: string
    createdAt?: string
    role?: number
}

export interface ModifyUserInfoType {
    nickname: string,
    avatar: string,
    gender: number,
    sign: string,
    birthday: string
}

export interface AdminModifyUserInfoType {
    uid: number,
    email: string,
    nickname: string,
    sign: string,
}

// 邮箱验证码发送实体
export interface EmailCodeType {
    email: string,
    code: string,
    uuid: string,
    username?: string,
    scene: Number // 1: 注册 2: 登录 3: 修改密码
}