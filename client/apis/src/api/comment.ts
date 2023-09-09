import request from '../request';
import type { AddCommentType } from '../types/comment-type';

// 评论
export const addCommentAPI = (postComment: AddCommentType) => {
    return request.post('v1/comment', postComment);
}

// 回复
export const addReplyAPI = (postComment: AddCommentType) => {
    return request.post('v1/comment/reply', postComment);
}

//获取评论
export const getCommentListAPI = (vid: number, page: number, page_size: number) => {
    return request.get(`v1/comment/${vid}/${page_size}/${page}`);
}

//获取回复
export const getReplyListAPI = (cid: string, page: number, page_size: number) => {
    return request.get(`v1/comment/reply/${cid}/${page_size}/${page}`);
}

//删除评论
export const deleteCommentAPI = (id: string,vid: number) => {
    return request.delete(`v1/comment/${vid}/${id}`);
}

//删除回复
export const deleteReplyAPI = (commentId: string, replyId: string, vid: number) => {
    console.log(vid);
    
    return request.delete(`v1/comment/reply/${vid}/${commentId}/${replyId}`);
}


// //获取管理评论
// export const getManageCommentListAPI = (vid: number, page: number, page_size: number) => {
//     return request.get(`v1/comment/list/manage?vid=${vid}&page=${page}&page_size=${page_size}`);
// }