
export interface sendWebSocketType {
    uid:number,
    roomId:number,
    cood:number,
    vid?:number,
    piv?:number
    msg?:String
    seek?:number 
}


export interface roomInfoType {
    id?:number,
    isPublic:boolean,
    password?:String | null,
    adminId?:number,
    userIds?:Array<number>,
    vid:number,
    pid:number,
    time?:number,
    isPaly?:boolean
}

export interface webSendMesgType {
    type: number,
    data?: any
}


// 房间聊天
export interface roomChatType {
    uid: number, //消息所属用户id
    // 消息所属用户头像
    avatar: string,
    // 消息所属用户昵称
    nickname: string,
    content: string, // 内容
    createdAt: string // 时间
}