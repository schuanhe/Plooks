package com.schuanhe.plooks.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.schuanhe.plooks.domain.Message;
import com.schuanhe.plooks.mapper.MessageMapper;
import com.schuanhe.plooks.service.MessageService;
import com.schuanhe.plooks.service.UserService;
import com.schuanhe.plooks.service.VideoService;
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


}
