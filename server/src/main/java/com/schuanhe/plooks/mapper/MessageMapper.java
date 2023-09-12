package com.schuanhe.plooks.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.schuanhe.plooks.domain.Message;

import java.util.List;

public interface MessageMapper extends BaseMapper<Message> {

    List<Message.Announces> getAnnouncement();

    List<Message.Announces> getImportantAnnouncement();

    List<Message.ReplyMessages> getReplyMessage(Integer uid);

    void insertReply(Message.ReplyMessages replyMessages);

    List<Message.AtMessages> getAtMessage(Integer uid);

    List<Message.LikeMessages> getLikeMessage(Integer uid);

    void insertAt(Message.AtMessages atMessages);

    void insertLike(Message.LikeMessages likeMessages);

    void addAnnounce(Message.Announces announces);

    void deleteAnnounce(Integer id);
}
