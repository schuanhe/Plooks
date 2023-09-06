export interface AddDanmukuType {
    vid: number,
    part: number,
    text?: string,
    mode?: 0 | 1,
    color?: string,
    time?: number,
    border?: boolean,
}
export interface DanmukuType{

    /**
     * 弹幕文本
     */
    text?: string;

    /**
     * 弹幕发送模式，0为滚动，1为静止
     */
    mode?: 0 | 1;

    /**
     * 弹幕颜色
     */
    color?: string;

    /**
     * 弹幕出现的时间，单位为秒
     */
    time?: number;

    /**
     * 弹幕是否有描边
     */
    border?: boolean;
}
