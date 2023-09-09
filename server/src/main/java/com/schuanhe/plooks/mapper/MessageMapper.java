package com.schuanhe.plooks.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.schuanhe.plooks.domain.Message;

import java.util.List;

public interface MessageMapper extends BaseMapper<Message> {

    List<Message.Announces> getAnnouncement();

    List<Message.Announces> getImportantAnnouncement();

    List<Message.ReplyMessages> getReplyMessage(Integer uid);

    void insertReply(Message.ReplyMessages replyMessages);
}
