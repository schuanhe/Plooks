package com.schuanhe.plooks.controller;


import com.schuanhe.plooks.domain.Message;
import com.schuanhe.plooks.service.MessageService;
import com.schuanhe.plooks.utils.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 消息相关接口
 */
@RestController
@RequestMapping("${base-url}/message")
public class MessageController {

    @Autowired
    private MessageService messageService;

    /**
     * 获取公告
     * @return 公告
     */
    @GetMapping("/announce/{size}/{page}")
    public ResponseResult<?> getAnnouncement(@PathVariable Integer size, @PathVariable Integer page) {
        List<Message.Announces> announcement = messageService.getAnnouncement(size, page);

        // 返回公告
        Map<String, Object> data = new HashMap<>();
        data.put("announces", announcement);
        data.put("total", announcement.size()); // TODO: 总数
        return ResponseResult.success(data);
    }

    /**
     * 获取重要公告
     */
    @GetMapping("/announce/important")
    public ResponseResult<?> getImportantAnnouncement() {
        List<Message.Announces> announcement = messageService.getImportantAnnouncement();

        // 返回公告
        Map<String, Object> data = new HashMap<>();
        data.put("announce", announcement.get(0));
        return ResponseResult.success(data);
    }

    /**
     * 获取回复消息
     */
    @GetMapping("/reply/{size}/{page}")
    public ResponseResult<?> getReply(@PathVariable Integer size, @PathVariable Integer page) {
        List<Message.ReplyMessages> replies = messageService.getReplyMessage(size, page);

        // 返回回复
        Map<String, Object> data = new HashMap<>();
        data.put("messages", replies);
        return ResponseResult.success(data);
    }

}
