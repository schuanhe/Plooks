import type { UserInfoType } from "./user-type";
import type { BaseVideoType } from "./video-type";

export interface ReplyMessageType {
    video: BaseVideoType,
    user: UserInfoType,
    createdAt: string,
    content: string,
    targetReplyContent: string,
    rootContent: string,
    commentId: number
}
