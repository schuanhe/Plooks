package com.schuanhe.plooks.controller;


import com.schuanhe.plooks.domain.Comments;
import com.schuanhe.plooks.service.CommentsService;
import com.schuanhe.plooks.utils.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 评论相关接口
 */
@RestController
@RequestMapping("${base-url}/comment")
public class CommentController {

    @Autowired
    private CommentsService commentsService;

    /**
     * 获取评论回复
     */
    @GetMapping("/{vid}/{size}/{page}")
    public ResponseResult<?> getComment(@PathVariable Integer vid, @PathVariable Integer size, @PathVariable Integer page ) {

        List<Comments.Comment> comments = commentsService.getComment(vid,size,page);

        Map<String,Object> data = new HashMap<>();
        data.put("comments",comments);
        return ResponseResult.success(data);
    }

}
