package com.schuanhe.plooks.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.schuanhe.plooks.domain.Comments;

import java.util.List;


public interface CommentsService extends IService<Comments>{

    /**
     * 获取评论
     * @param vid 视频id
     * @param size 每页数量
     * @param page 页码
     * @return 评论列表
     */
    List<Comments.Comment> getComment(Integer vid, Integer size, Integer page);
}
