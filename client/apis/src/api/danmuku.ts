import request from '../request';
import type { AddDanmukuType } from '../types/danmuku-type';

//发送弹幕
export const sendDanmukuAPI = (Danmuku: AddDanmukuType) => {
    return request.post('v1/danmuku', Danmuku);
}

//获取弹幕
export const getDanmukuAPI = (vid: number, part: number) => {
    return request.get(`v1/danmuku/${vid}/${part}`);
}
