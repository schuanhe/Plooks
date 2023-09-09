import request from '../request';

//获取回复通知
export const getReplyMessageAPI = (page: number, page_size: number) => {
    return request.get(`v1/message/reply/${page_size}/${page}`);
}
