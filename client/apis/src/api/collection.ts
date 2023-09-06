import request from '../request';
import type { ModifyCollectionType } from "../types/collection-type";

//获取收藏夹列表
export const getCollectionListAPI = () => {
    return request.get('v1/collection');
}

//创建收藏夹
export const addCollectionAPI = (name: string) => {
    return request.post('v1/collection', { name });
}

//修改收藏夹
export const modifyCollectionAPI = (collect: ModifyCollectionType) => {
    return request.put('v1/collection', collect);
}

//获取收藏夹信息(非所有者仅获取公开的)
export const getCollectionInfoAPI = (id: number) => {
    return request.get(`v1/collection/${id}`);
}

//删除收藏夹
export const deleteCollectionAPI = (id: number) => {
    return request.delete('v1/collection/' + id );
}