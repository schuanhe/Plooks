import request from '../request';
import type { ModifyVideoType, UploadVideoType } from '../types/video-type';

//上传视频信息
export const uploadVideoInfoAPI = (uploadVideo: UploadVideoType) => {
    return request.post('v1/video/info', uploadVideo);
}

//获取视频状态
export const getVideoStatusAPI = (vid: number) => {
    return request.get('v1/video/status/' + vid);
}

//修改视频信息
export const modifyVideoInfoAPI = (modifyVideo: ModifyVideoType) => {
    return request.put('v1/video/info', modifyVideo);
}

//获取视频信息
export const getVideoInfoAPI = (vid: number) => {
    return request.get(`v1/video/${vid}`);
}

//获取视频列表
export const getVideoListAPI = (page: number, pageSize: number, partitionId: number) => {
    return request.get(`v1/video/list/${partitionId}/${pageSize}/${page}`);
}

//获取推荐视频列表
export const getRecommendedVideoAPI = (pageSize: number) => {
    return request.get(`v1/video/recommended/${pageSize}`);
}

// 搜索视频
export const searchVideoAPI = (page: number, pageSize: number, keywords: string) => {
    return request.get(`v1/video/search/${keywords}/${pageSize}/${page}`);
}

//提交审核
export const submitReviewAPI = (id: number) => {
    return request.post('v1/video/review/' + id);
}

//获取收藏夹内的视频
export const getCollectVideoAPI = (id: number, page: number, page_size: number) => {
    return request.get(`v1/video/collect/${id}/${page_size}/${page}`);
}

//获取我的视频
export const getUploadVideoAPI = (page: number, page_size: number) => {
    return request.get(`v1/video/upload/${page_size}/${page}`);
}

//通过用户ID获取视频
export const getVideoListByUidAPI = (uid: number, page: number, page_size: number) => {
    return request.get(`v1/video/user/list/${uid}/${page_size}/${page}`);
}

//删除视频 
export const deleteVideoAPI = (id: number) => {
    return request.delete(`v1/video/${id}`, );
}

//管理员获取视频列表
export const adminGetVideoListAPI = (page: number, pageSize: number, partitionId: number) => {
    return request.get(`v1/video/admin/${pageSize}/${page}/${partitionId}`);
}

//管理员搜索视频
export const adminSearchVideoAPI = (page: number, pageSize: number, keywords: string) => {
    return request.get(`v1/video/admin/search/${keywords}/${pageSize}/${page}`);
}

//管理员删除视频
export const adminDeleteVideoAPI = (vid: number) => {
    return request.delete(`v1/video/admin/${vid}`, );
}

//获取审核视频列表
export const getReviewListAPI = (page: number, page_size: number) => {
    return request.get(`v1/video/admin/review/${page_size}/${page}`);
}

//获取审核资源列表
export const getResourceListAPI = (vid: number) => {
    return request.get(`v1/video/admin/review/resource/${vid}`);
}

//审核视频
export const reviewVideoAPI = (vid: number, status: number) => {
    return request.put('v1/video/admin/review/video', { vid, status });
}

//审核资源
export const reviewResourceAPI = (id: number, status: number) => {
    return request.put('v1/video/admin/review/resource', { id, status });
}