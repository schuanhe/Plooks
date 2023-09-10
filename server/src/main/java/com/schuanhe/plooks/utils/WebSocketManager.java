package com.schuanhe.plooks.utils;


import com.alibaba.fastjson.JSON;

import java.util.Base64;
import java.util.List;
import java.util.Map;
import javax.websocket.Session;

import java.util.concurrent.ConcurrentHashMap;

/**
 * WebSocket管理器
 */
public class WebSocketManager {

    // 私聊链接
    public static Map<Integer, Session> whispers = new ConcurrentHashMap<>();

    // 视频在线链接(vid,(cid,session)))
    public static Map<Integer, Map<String,Session>> videoOnline = new ConcurrentHashMap<>();

    /**
     * 发送消息
     */
    public static <T> void sendMessage(Integer uid, T message) {
        Session session = whispers.get(uid);
        if (session != null) {
            try {
                String s = JSON.toJSONString(message);
                // 通过base64加密
                String encode = Base64.getEncoder().encodeToString(s.getBytes());
                session.getAsyncRemote().sendText(encode);
            } catch (Exception e) {
                System.out.println("发送消息失败：" + e.getMessage());
                return;
            }
        }
        System.out.println("当前在线人数：" + whispers.size());
    }


}
