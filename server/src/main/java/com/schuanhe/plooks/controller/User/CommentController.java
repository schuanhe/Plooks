package com.schuanhe.plooks.controller.User;


import com.schuanhe.plooks.domain.Comments;
import com.schuanhe.plooks.domain.form.ReplyForm;
import com.schuanhe.plooks.service.User.CommentsService;
import com.schuanhe.plooks.utils.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
     * 获取评论
     */
    @GetMapping("/{vid}/{size}/{page}")
    public ResponseResult<?> getComment(@PathVariable Integer vid, @PathVariable Integer size, @PathVariable Integer page ) {

        List<Comments.Comment> comments = commentsService.getComment(vid,size,page);

        Map<String,Object> data = new HashMap<>();
        data.put("comments",comments);
        return ResponseResult.success(data);
    }

    /**
     * 获取回复
     */
    @GetMapping("/reply/{cid}/{size}/{page}")
    public ResponseResult<?> getReply(@PathVariable Integer cid, @PathVariable Integer size, @PathVariable Integer page ) {

        List<Comments.Reply> replies = commentsService.getReply(cid,size,page);

        Map<String,Object> data = new HashMap<>();
        data.put("replies",replies);
        return ResponseResult.success(data);
    }

    /**
     * 发送评论
     */
    @PostMapping
    public ResponseResult<?> sendComment(@RequestBody Comments.Comment comment){
        Integer id = commentsService.sendComment(comment);
        Map<String,Integer> data = new HashMap<>();
        data.put("id",id);
        return ResponseResult.success(data);
    }

    /**
     * 发送回复
     */
    @PostMapping("/reply")
    public ResponseResult<?> sendReply(@RequestBody ReplyForm reply){
        Integer id = commentsService.sendReply(reply);
        Map<String,Integer> data = new HashMap<>();
        data.put("id",id);
        return ResponseResult.success(data);
    }

    /**
     * 删除评论
     */
    @DeleteMapping("/{vid}/{id}")
    public ResponseResult<?> deleteComment(@PathVariable Integer id, @PathVariable Integer vid){
        commentsService.removeCommentById(id,vid);
        return ResponseResult.success();
    }

    /**
     * 删除回复
     */
    @DeleteMapping("/reply/{vid}/{fid}/{id}")
    public ResponseResult<?> deleteReply(@PathVariable Integer id, @PathVariable Integer fid,@PathVariable Integer vid){
        commentsService.removeReplyById(id,fid,vid);
        return ResponseResult.success();
    }


}
