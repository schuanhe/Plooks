import type { UserInfoType } from "./user-type";

export interface WhisperType {
    fid: number,
    content: string,
}

export interface WhisperListType {
    user: UserInfoType,
    createdAt: string | Date,
    status: boolean
}

export interface WhisperDetailsType {
    fid: number,
    from_id: number,
    content: string,
    createdAt: string
}