import request from '../request';
import type { sendWebSocketType, roomInfoType } from '../types/watchroom-type';

// 发送webSocket消息
export const sendWebSocketAPI = (sendWebSocket:sendWebSocketType) => {
    return request.post('v1/watchroom/msg',sendWebSocket)
}
// 创建房间
export const addRoomApi = (addRoom:roomInfoType) => {
    return request.post('v1/watchroom',addRoom)
}
// 进入房间
export const inRoomApi = (rid:number, password:String|null) => {
    return request.post(`v1/watchroom/${rid}`, { password } )
}
// 获取房间信息
export const getRoomInfoApi = (rid:number) => {
    return request.get(`v1/watchroom/${rid}`)
}
// 更新房间信息
export const updateRoomApi = (rid:number, roomInfo:roomInfoType) => {
    return request.put(`v1/watchroom/${rid}`, roomInfo)
}