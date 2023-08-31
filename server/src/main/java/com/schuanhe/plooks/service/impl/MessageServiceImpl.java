package com.schuanhe.plooks.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.schuanhe.plooks.domain.Message;
import com.schuanhe.plooks.mapper.MessageMapper;
import com.schuanhe.plooks.service.MessageService;
import com.schuanhe.plooks.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message> implements MessageService {

    @Autowired
    private RedisCache redisCache;

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
}
