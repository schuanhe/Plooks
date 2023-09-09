package com.schuanhe.plooks.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.schuanhe.plooks.domain.Message;

import java.util.List;

public interface MessageService extends IService<Message>{
    List<Message.Announces> getAnnouncement(Integer size, Integer page);

    List<Message.Announces> getImportantAnnouncement();

    List<Message.ReplyMessages> getReplyMessage(Integer size, Integer page);

    void sendReplyMessage(Message.ReplyMessages replyMessages);
}
