import type { BaseVideoType } from "./video-type"

export interface AddHistoryType {
    vid: number,
    part: number,
    time: number
}

export interface HistoryVideoType {
    id: number,
    part: number,
    time: number,
    video: BaseVideoType,
    createdAt: string
}


