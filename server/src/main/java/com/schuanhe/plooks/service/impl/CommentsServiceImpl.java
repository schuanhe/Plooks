package com.schuanhe.plooks.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.schuanhe.plooks.domain.Comments;
import com.schuanhe.plooks.domain.Message;
import com.schuanhe.plooks.mapper.CommentsMapper;
import com.schuanhe.plooks.mapper.MessageMapper;
import com.schuanhe.plooks.service.CommentsService;
import com.schuanhe.plooks.service.MessageService;
import com.schuanhe.plooks.service.UserService;
import com.schuanhe.plooks.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


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
                        // 有很多评论没用回复，所以不用从数据库中获取
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
}
