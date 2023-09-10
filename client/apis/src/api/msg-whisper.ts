import request from '../request';
import type { WhisperType } from '../types/msg-whisper-type';

//获取私信列表
export const getWhisperListAPI = () => {
    return request.get('v1/whisper')
}

//获取私信列表
export const getWhisperDetailsAPI = (fid: number, page: number, page_size: number) => {
    return request.get(`v1/whisper/${fid}/${page_size}/${page}`)
}

//发送私信
export const sendWhisperAPI = (msg: WhisperType) => {
    return request.post('v1/whisper', msg)
}

//已读私信
export const readWhisperAPI = (id: number) => {
    return request.put(`v1/whisper/${id}` )
}