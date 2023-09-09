import request from '../request';

//获取@通知
export const getAtMessageAPI = (page: number, page_size: number) => {
    return request.get(`v1/message/at/${page_size}/${page}`);
}
