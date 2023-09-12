import request from '../request'
import type { AddPartitionType } from '../types/partition-type';

//获取分区
export const getPartitionAPI = () => {
    return request.get(`v1/partition`);
}

//新增分区
export const addPartitionAPI = (addPartition: AddPartitionType) => {
    return request.post(`v1/partition/admin`, addPartition);
}


//删除分区
export const deletePartitionAPI = (id: number) => {
    return request.delete(`v1/partition/admin/${id}`);
}
