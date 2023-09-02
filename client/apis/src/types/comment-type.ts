import type { UserInfoType } from "./user-type"

export interface AddCommentType {
    vid: number,  //视频id
    content: string, //评论内容
    parentId?: string, //父级评论id
    replyUserId?: number, //回复用户id
    replyContent?: string, //回复内容
    at: Array<string> //被@的用户id
}


export interface CommentType {
    id: string, //评论id
    content: string, //评论内容
    author: UserInfoType, //评论作者
    reply: Array<ReplyType>, //回复
    createdAt: number, //评论时间
    page: number,//回复页码
    noMore: boolean //是否还有更多回复
}

export interface ReplyType {
    id: string, //回复id
    content: string, //回复内容
    author: UserInfoType, //回复作者
    createdAt: number, //回复时间
}