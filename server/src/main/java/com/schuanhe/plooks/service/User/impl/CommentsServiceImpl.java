package com.schuanhe.plooks.service.User.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.schuanhe.plooks.domain.Comments;
import com.schuanhe.plooks.domain.Message;
import com.schuanhe.plooks.domain.form.ReplyForm;
import com.schuanhe.plooks.mapper.CommentsMapper;
import com.schuanhe.plooks.service.User.CommentsService;
import com.schuanhe.plooks.service.User.MessageService;
import com.schuanhe.plooks.service.User.UserService;
import com.schuanhe.plooks.service.User.VideoService;
import com.schuanhe.plooks.utils.RedisCache;
import com.schuanhe.plooks.utils.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class CommentsServiceImpl extends ServiceImpl<CommentsMapper, Comments> implements CommentsService {

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private UserService userService;

    @Autowired
    private MessageService messageService;

    @Autowired
    private VideoService videoService;


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
                    // 获取评论回复前两天
                    if (!comment.isNoMore()){
                        comment.setReply(this.getReply(comment.getId(),2,1));
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
        reply1.setCreatedAt(new Date());

        // 处理@用户
        List<Integer> atIds = new ArrayList<>();
        reply.getAt().forEach(at -> {
            Integer atId = userService.getUserIdByNickName(at);
            // 如果@的用户存在 并且不是回复者本人，并且不为0
            if (atId != null && !atId.equals(uid) && atId != 0){
                messageService.sendAtMessage(new Message.AtMessages(reply.getVid(),atId,uid));
                atIds.add(atId);
            }
        });
        reply1.setAt(atIds);

        if (reply.getReplyUserId() != null && reply.getReplyUserId() != 0){
            Message.ReplyMessages replyMessages = new Message.ReplyMessages(reply.getVid(),
                    reply.getReplyUserId(),
                    uid,
                    reply.getContent(),
                    reply.getReplyContent(),
                    reply.getRootComment(),
                    reply.getParentId()
            );
            messageService.sendReplyMessage(replyMessages);
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
    public List<Comments.Reply> getReply(Integer cid, Integer size, Integer page) {
        // 获取评论回复（先从redis中获取）
        List<Comments.Reply> replies = redisCache.getCacheList("comments:reply:list:" + cid,(page- 1) * size,page * size - 1);
        // 如果redis中没有评论回复，且三分内没刷新过就从数据库中获取
        if (replies.size() > 0)
            return replies;
        // 获取缓存对象
        Object cacheObject = redisCache.getCacheObject("refresh:reply:" + cid);
        boolean ref = cacheObject != null && (boolean) cacheObject;
        // 根据缓存对象是否为null来设置refresh变量
        if (!ref){
            // 从数据库中获取评论回复
            List<Comments.Reply> newReplies = baseMapper.getReply(cid);
            // 获取回复者信息
            newReplies.forEach(reply -> reply.setAuthor(userService.getUserInfoById(reply.getUid())));
            // 设置三分钟内不刷新
            redisCache.setCacheObject("refresh:reply:" + cid,true,180);
            // 将评论回复存入redis
            if (newReplies.size() > 0){
                //先删除redis中的评论回复
                redisCache.deleteObject("comments:reply:list:" + cid);
                redisCache.setCacheList("comments:reply:list:" + cid,newReplies);
                // 从redis中获取评论回复->因为redis工具类中会先进后出
                replies = redisCache.getCacheList("comments:reply:list:" + cid,(page- 1) * size,page * size - 1);
            }
        }
        return replies;
    }

    @Override
    public void removeCommentById(Integer id,Integer vid) {
        // 删除评论
        baseMapper.deleteCommentById(id);
        // 删除所有回复
        baseMapper.deleteReplyByFid(id);
        // 删除redis中的评论
        redisCache.deleteObject("comments:comment:list:" + vid);
        // 删除redis中的回复
        redisCache.deleteObject("comments:reply:list:" + id);

        // 删除缓存
        redisCache.deleteObject("refresh:comments:" + vid);
        System.out.println("删除评论" + id + "成功" +vid);
    }

    @Override
    public void removeReplyById(Integer id,Integer fid,Integer vid) {
        // 删除回复
        baseMapper.deleteReplyById(id);
        // 删除redis中的回复
        redisCache.deleteObject("comments:reply:list:" + fid);
        redisCache.deleteObject("comments:comment:list:" + vid);

        // 删除缓存
        redisCache.deleteObject("refresh:reply:" + fid);
        redisCache.deleteObject("refresh:comments:" + vid);
        System.out.println("删除回复" + id + "成功" +fid + "v:" + vid);
    }


    @Override
    public Integer sendComment(Comments.Comment comment) {
        Integer uid = WebUtils.getUserId();
        if (uid == null){
            return null;
        }


        List<Integer> atIds = new ArrayList<>();
        // 获取@用户的id
        comment.getAt().forEach(at -> {
            // 获取@用户的id
            Integer atId = userService.getUserIdByNickName(at);

            // 判断重复@用户
            if (atIds.contains(atId)){
                return;
            }
            atIds.add(atId);

            // 被@者id不能是自己，且不能是0
            if (atId != null && !atId.equals(uid) && atId != 0){
                messageService.sendAtMessage(new Message.AtMessages(comment.getVid(), atId, uid));
            }
        });


        Integer uidByVid = videoService.getUidByVid(comment.getVid());
        // 给视频作者发送消息
        Message.ReplyMessages commentMessages = new Message.ReplyMessages(comment.getVid(),
                uidByVid,
                uid,
                comment.getContent()
        );
        // 发送消息
        messageService.sendReplyMessage(commentMessages);

        // 设置评论者id
        comment.setUid(uid);
        comment.setCreatedAt(new Date());
        // 保存评论
        baseMapper.insertComment(comment);
        // 获取评论者消息
        comment.setAuthor(userService.getUserInfoById(comment.getUid()));
        // 新增清redis中的评论
        redisCache.setCacheList("comments:comment:list:" + comment.getVid(),comment);

        return comment.getId();
    }

}
