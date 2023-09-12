package com.schuanhe.plooks.service.Admin.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.schuanhe.plooks.domain.Message;
import com.schuanhe.plooks.mapper.MessageMapper;
import com.schuanhe.plooks.service.Admin.AnnouncesAdminService;
import com.schuanhe.plooks.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AnnouncesAdminServiceImpl extends ServiceImpl<MessageMapper, Message> implements AnnouncesAdminService {

    @Autowired
    private RedisCache redisCache;

    @Override
    public void addAnnounce(Message.Announces announces) throws Exception{
        if (announces.getTitle() == null || announces.getContent() == null|| announces.getImportant() == null) {
            throw new Exception("参数错误");
        }
        baseMapper.addAnnounce(announces);
        if (announces.getImportant()) {
            redisCache.deleteObject("importantAnnouncement");
        }
        redisCache.deleteObject("announcement");
    }

    @Override
    public void deleteAnnounce(Integer id) {
        baseMapper.deleteAnnounce(id);
        redisCache.deleteObject("announcement");
        redisCache.deleteObject("importantAnnouncement");
    }
}
