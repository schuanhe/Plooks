package com.schuanhe.plooks.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.schuanhe.plooks.domain.Comments;

import java.util.List;

public interface CommentsMapper extends BaseMapper<Comments> {
    List<Comments.Comment> getComment(Integer vid);

    void insertComment(Comments.Comment comment);

    void insertReply(Comments.Reply reply);

    void updateNoMore(Integer id);
}
