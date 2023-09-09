package com.schuanhe.plooks.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.schuanhe.plooks.domain.Message;

import java.util.List;

public interface MessageService extends IService<Message>{
    List<Message.Announces> getAnnouncement(Integer size, Integer page);

    List<Message.Announces> getImportantAnnouncement();

    List<Message.ReplyMessages> getReplyMessage(Integer size, Integer page);

    List<Message.AtMessages> getAtMessage(Integer size, Integer page);

    List<Message.LikeMessages> getLikeMessage(Integer size, Integer page);

    void sendReplyMessage(Message.ReplyMessages replyMessages);

    void sendAtMessage(Message.AtMessages atMessages);

    void sendLikeMessage(Message.LikeMessages likeMessages);
}
