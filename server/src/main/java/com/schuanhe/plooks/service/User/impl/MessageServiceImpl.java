package com.schuanhe.plooks.service.User.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.schuanhe.plooks.domain.Message;
import com.schuanhe.plooks.mapper.MessageMapper;
import com.schuanhe.plooks.service.User.MessageService;
import com.schuanhe.plooks.service.User.UserService;
import com.schuanhe.plooks.service.User.VideoService;
import com.schuanhe.plooks.utils.RedisCache;
import com.schuanhe.plooks.utils.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message> implements MessageService {

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private UserService userService;

    @Autowired
    private VideoService videoService;

    @Override
    public List<Message.Announces> getAnnouncement(Integer size, Integer page) {
        // 先获取redis中的公告
        List<Message.Announces> announces = redisCache.getCacheList("announcement",(page-1)*size,page*size-1);
        // 如果redis中没有公告，就从数据库中获取
        if (announces == null || announces.size() == 0) {
            // 从数据库中获取公告
            List<Message.Announces> newAnnounces = baseMapper.getAnnouncement();
            // 将公告存入redis
            if (newAnnounces != null && newAnnounces.size() > 0) {
                redisCache.setCacheList("announcement", newAnnounces);
                // 从redis中获取公告
                announces = redisCache.getCacheList("announcement",(page-1)*size,page*size-1);
            }

        }

        return announces;

    }

    @Override
    public List<Message.Announces> getImportantAnnouncement() {
        // 先获取redis中的公告
        List<Message.Announces> announces = redisCache.getCacheList("importantAnnouncement",0,-1);
        // 如果redis中没有公告，就从数据库中获取
        if (announces == null || announces.size() == 0) {
            // 从数据库中获取公告
            List<Message.Announces> newAnnounces = baseMapper.getImportantAnnouncement();
            // 将公告存入redis
            if (newAnnounces != null && newAnnounces.size() > 0) {
                redisCache.setCacheList("importantAnnouncement", newAnnounces);
                // 从redis中获取公告
                announces = redisCache.getCacheList("importantAnnouncement",0,-1);
            }

        }

        return announces;
    }

    @Override
    public List<Message.ReplyMessages> getReplyMessage(Integer size, Integer page) {
        // 获取uid
        Integer uid = WebUtils.getUserId();
        if (uid == null) {
            return null;
        }
        // 先获取redis中的回复消息
        List<Message.ReplyMessages> replyMessages = redisCache.getCacheList("message:reply:" + uid,(page-1)*size,page*size-1);
        // 如果redis中没有回复消息，就从数据库中获取
        if (replyMessages == null || replyMessages.size() == 0) {
            // 从数据库中获取回复消息
            List<Message.ReplyMessages> newReplyMessages = baseMapper.getReplyMessage(uid);
            // 将回复消息存入redis
            if (newReplyMessages != null && newReplyMessages.size() > 0) {
                // 获取发送者用户信息,和视频信息
                for (Message.ReplyMessages replyMessage : newReplyMessages) {
                    replyMessage.setUser(userService.getUserInfoById(replyMessage.getFid()));
                    replyMessage.setVideo(videoService.getVideoInfo(replyMessage.getVid()));
                }
                redisCache.setCacheList("message:reply:" + uid, newReplyMessages);
                // 从redis中获取回复消息
                replyMessages = redisCache.getCacheList("message:reply:" + uid,(page-1)*size,page*size-1);
            }

        }

        return replyMessages;
    }

    @Override
    public void sendReplyMessage(Message.ReplyMessages replyMessages) {

        // 获取发送者用户信息,和视频信息
        replyMessages.setUser(userService.getUserInfoById(replyMessages.getFid()));
        replyMessages.setVideo(videoService.getVideoInfo(replyMessages.getVid()));

        // 将回复消息存入数据库
        baseMapper.insertReply(replyMessages);
        // 将回复消息存入redis
        redisCache.setCacheList("message:reply:" + replyMessages.getUid(), replyMessages);
    }

    @Override
    public void sendAtMessage(Message.AtMessages atMessages) {
        // 获取发送者用户信息,和视频信息
        atMessages.setUser(userService.getUserInfoById(atMessages.getFid()));
        atMessages.setVideo(videoService.getVideoInfo(atMessages.getVid()));

        // 将@消息存入数据库
        baseMapper.insertAt(atMessages);
        // 将@消息存入redis
        redisCache.setCacheList("message:at:" + atMessages.getUid(), atMessages);
    }

    @Override
    public void sendLikeMessage(Message.LikeMessages likeMessages) {
        // 获取发送者用户信息,和视频信息
        likeMessages.setUser(userService.getUserInfoById(likeMessages.getFid()));
        likeMessages.setVideo(videoService.getVideoInfo(likeMessages.getVid()));

        // 将点赞消息存入数据库
        baseMapper.insertLike(likeMessages);
        // 将点赞消息存入redis
        redisCache.setCacheList("message:like:" + likeMessages.getUid(), likeMessages);

    }

    @Override
    public Integer getAnnouncementContent() {

        return baseMapper.getAnnouncementContent();
    }

    @Override
    public List<Message.AtMessages> getAtMessage(Integer size, Integer page) {
        // 获取uid
        Integer uid = WebUtils.getUserId();
        if (uid == null) {
            return null;
        }
        // 先获取redis中的@消息
        List<Message.AtMessages> atMessages = redisCache.getCacheList("message:at:" + uid,(page-1)*size,page*size-1);
        // 如果redis中没有@消息，就从数据库中获取
        if (atMessages == null || atMessages.size() == 0) {
            // 从数据库中获取@消息
            List<Message.AtMessages> newAtMessages = baseMapper.getAtMessage(uid);
            // 将@消息存入redis
            if (newAtMessages != null && newAtMessages.size() > 0) {
                // 获取发送者用户信息,和视频信息
                for (Message.AtMessages atMessage : newAtMessages) {
                    atMessage.setUser(userService.getUserInfoById(atMessage.getFid()));
                    atMessage.setVideo(videoService.getVideoInfo(atMessage.getVid()));
                }
                redisCache.setCacheList("message:at:" + uid, newAtMessages);
                // 从redis中获取@消息
                atMessages = redisCache.getCacheList("message:at:" + uid,(page-1)*size,page*size-1);
            }

        }

        return atMessages;
    }

    @Override
    public List<Message.LikeMessages> getLikeMessage(Integer size, Integer page) {
        // 获取uid
        Integer uid = WebUtils.getUserId();
        if (uid == null) {
            return null;
        }
        // 先获取redis中的点赞消息
        List<Message.LikeMessages> likeMessages = redisCache.getCacheList("message:like:" + uid,(page-1)*size,page*size-1);
        // 如果redis中没有点赞消息，就从数据库中获取
        if (likeMessages == null || likeMessages.size() == 0){
            // 从数据库中获取点赞消息
            List<Message.LikeMessages> newLikeMessages = baseMapper.getLikeMessage(uid);
            // 将点赞消息存入redis
            if (newLikeMessages != null && newLikeMessages.size() > 0) {
                // 获取发送者用户信息,和视频信息
                for (Message.LikeMessages likeMessage : newLikeMessages) {
                    likeMessage.setUser(userService.getUserInfoById(likeMessage.getFid()));
                    likeMessage.setVideo(videoService.getVideoInfo(likeMessage.getVid()));
                }
                redisCache.setCacheList("message:like:" + uid, newLikeMessages);
                // 从redis中获取点赞消息
                likeMessages = redisCache.getCacheList("message:like:" + uid,(page-1)*size,page*size-1);
            }
        }
        return likeMessages;
    }


}
