import type { AddDanmukuType, DanmukuType } from "@plooks/apis";

export interface QualityType {
    [key: number]: {
        name?: string,
        url: string,
        type?: string
    }
}

export interface DanmukuOptionsType {
    open: boolean,
    placeholder?: string,
    data?: Array<DanmukuType>,
    send?: (Danmuku: AddDanmukuType) => void
}


export interface OptionsType {
    resource: string | qualityType, // 资源
    cover?: string, // 封面
    type?: string,//视频类型
    mobile?: boolean,//移动端
    blob?: boolean,//mp4视频是否使用blob
    customType?: (player: HTMLVideoElement, src: string) => void,
    customQualityChange?: (quality: string) => void, // 自定义清晰度切换
    theme?: string,//主题色,
    Danmuku?: DanmukuOptionsType,
    playbackSpeed?: Array<number>,// 播放速度
}


export interface OptionType {
    type?: string,//视频类型
    currentTime: number,//视频进度
    resource: string | qualityType
    cover?: string, // 封面
    part: number, // 分P
    Danmuku: Array<DanmukuType>, // 弹幕
}
