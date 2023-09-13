import request from '../request';
import type { BaseResourceType } from '../types/resource-type';

//修改资源标题
export const modifyTitleAPI = (resourceTitle: BaseResourceType) => {
    return request.put('v1/resource', resourceTitle);
}

//删除资源
export const deleteResourceAPI = (id: number) => {
    return request.delete('v1/resource/' + id);
}