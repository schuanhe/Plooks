import type { DanmakuType } from "@plooks/apis";

export interface QualityType {
    [key: number]: {
        name?: string,
        url: string,
        type?: string
    }
}

export interface DanmakuOptionsType {
    open: boolean,
    placeholder?: string,
    data?: Array<danmakuType>,
    send?: (danmaku: danmakuType) => void
}


export interface OptionsType {
    resource: string | qualityType,
    cover?: string,
    type?: string,//视频类型
    mobile?: boolean,//移动端
    blob?: boolean,//mp4视频是否使用blob
    customType?: (player: HTMLVideoElement, src: string) => void,
    customQualityChange?: (quality: string) => void,
    theme?: string,//主题色,
    danmaku?: danmakuOptionsType,
    playbackSpeed?: Array<number>,// 播放速度
}
