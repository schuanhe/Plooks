package com.schuanhe.plooks.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.schuanhe.plooks.domain.Comments;
import com.schuanhe.plooks.domain.form.ReplyForm;
import com.schuanhe.plooks.mapper.CommentsMapper;
import com.schuanhe.plooks.service.CommentsService;
import com.schuanhe.plooks.service.UserService;
import com.schuanhe.plooks.utils.RedisCache;
import com.schuanhe.plooks.utils.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Service
public class CommentsServiceImpl extends ServiceImpl<CommentsMapper, Comments> implements CommentsService {

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private UserService userService;


    @Override
    public List<Comments.Comment> getComment(Integer vid, Integer size, Integer page) {
        // 先获取redis中的评论
        List<Comments.Comment> comments = redisCache.getCacheList("comments:comment:list:" + vid,(page- 1) * size,page * size - 1);
        // 如果redis中没有评论，且三分内没刷新过就从数据库中获取
        // 获取缓存对象
        Object cacheObject = redisCache.getCacheObject("refresh:comments:" + vid);

        // 根据缓存对象是否为null来设置refresh变量
        boolean refresh = cacheObject != null && (boolean) cacheObject;

        if (comments.size() == 0 && !refresh){
            // 从数据库中获取评论
            List<Comments.Comment> newComments = baseMapper.getComment(vid);
            // 将评论存入redis
            if (newComments.size() > 0){
                newComments.forEach(comment -> {
                    // 获取评论者消息
                    comment.setAuthor(userService.getUserInfoById(comment.getUid()));
                    // 获取评论回复
                    if (comment.isNoMore()){
                        // 获取评论回复（先从redis中获取）
                        List<Comments.Reply> replies = redisCache.getCacheList("comments:reply:list:" + comment.getId(),0,2);

                        // 如果redis中没有评论回复，且三分内没刷新过就从数据库中获取
                        // 获取缓存对象
                        Object ref = redisCache.getCacheObject("refresh:reply:" + comment.getId());
                        // 根据缓存对象是否为null来设置refresh变量
                        comment.setReply(replies);
                    }
                });
                //先删除redis中的评论
                redisCache.deleteObject("comments:comment:list:" + vid);
                redisCache.setCacheList("comments:comment:list:" + vid,newComments);
                // 从redis中获取评论
                comments = redisCache.getCacheList("comments:comment:list:" + vid,(page- 1) * size,page * size - 1);
            }
            // 设置三分钟内不刷新
            redisCache.setCacheObject("refresh:comments:" + vid,true,180);
        }
        return comments;

    }

    @Override
    public Integer sendReply(ReplyForm reply) {
        Integer uid = WebUtils.getUserId();
        if (uid == null){
            return null;
        }
        // 设置回复者id
        Comments.Reply reply1 = new Comments.Reply();
        reply1.setUid(uid);
        reply1.setContent(reply.getContent());
        reply1.setFid(reply.getParentId());
        reply1.setVid(reply.getVid());

        // 处理@用户
        List<Integer> atIds = new ArrayList<>();
        reply.getAt().forEach(at -> {
            Integer atId = userService.getUserIdByNickName(at);
            if (atId != null) {
                //TODO: 给被@者发送消息
                atIds.add(atId);
            }
        });
        reply1.setAt(atIds);

        if (reply.getReplyUserId() != null && reply.getReplyUserId() != 0){
            //TODO: 给回复者发送消息
        }

        // 设置父id的noMore为true
        baseMapper.updateNoMore(reply.getParentId());

        // 删除redis中的评论缓存，和刷新缓存
        redisCache.deleteObject("comments:comment:list:" + reply.getVid());
        redisCache.deleteObject("refresh:comments:" + reply.getVid());

        // 保存回复
        baseMapper.insertReply(reply1);

        // 获取回复者消息
        reply1.setAuthor(userService.getUserInfoById(reply1.getUid()));

        // 新增清redis中的回复
        redisCache.setCacheList("comments:reply:list:" + reply1.getFid(),reply1);
        return reply1.getId();
    }

    @Override
    public Integer sendComment(Comments.Comment comment) {
        Integer uid = WebUtils.getUserId();
        if (uid == null){
            return null;
        }
        // 设置评论者id
        comment.setUid(uid);

        // 保存评论
        baseMapper.insertComment(comment);
        // 新增清redis中的评论
        redisCache.setCacheList("comments:comment:list:" + comment.getVid(),comment);

        return comment.getId();
    }

}
