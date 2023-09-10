package com.schuanhe.plooks.controller.WebSocket;


import com.alibaba.fastjson.JSON;
import com.schuanhe.plooks.utils.JwtUtil;
import com.schuanhe.plooks.utils.WebSocketManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@Slf4j
@Component
@ServerEndpoint("/api/v1/websocket/video/online/{vid}/{cid}")
//此注解相当于设置访问URL
public class VideoOnlineWebSocket {
    /**
     *  与某个客户端的连接对话，需要通过它来给客户端发送消息
     */
    public Session session;

    public Integer vid;

    public String cid;


    @OnOpen
    public void onOpen(Session session, @PathParam(value="vid")Integer vid, @PathParam(value="cid")String cid) {
        this.session = session;
        this.cid = cid;
        this.vid = vid;
        Map<String, Session> vidMap = WebSocketManager.videoOnline.get(vid);
        if(vidMap == null || vidMap.size() == 0) {
            // 说明是第一个链接
            vidMap = new ConcurrentHashMap<>();
            vidMap.put(cid,session);
            WebSocketManager.videoOnline.put(vid,vidMap);
        }else {
            vidMap.put(cid,session);
        }
        WebSocketManager.videoOnline.put(vid,vidMap);
        sendOnlineNum(vid);
        log.info("[链接成功]vid={} cid={} 当前房间人数={}",vid,cid,WebSocketManager.videoOnline.get(vid).size());
    }

    @OnClose
    public void onClose() {
        Map<String, Session> vidMap = WebSocketManager.videoOnline.get(vid);
        if(vidMap != null) {
            vidMap.remove(cid);
            WebSocketManager.videoOnline.put(vid,vidMap);
            sendOnlineNum(vid);
        }
        log.info("[断开连接]cid={} 当前房间人数{}",cid,WebSocketManager.videoOnline.get(vid).size());
    }

    // 发送更新在线人数的消息
    public static void sendOnlineNum(Integer vid) {
        Map<String, Session> vidMap = WebSocketManager.videoOnline.get(vid);
        if(vidMap == null) {
            return;
        }
        for (Map.Entry<String, Session> entry : vidMap.entrySet()) {
            Session session = entry.getValue();
            try {
                int size = vidMap.size();
                Map<String, Integer> map = new HashMap<>();
                map.put("number",size);
                session.getBasicRemote().sendText(JSON.toJSONString(map));
            } catch (Exception e) {
                log.error("[发送消息]失败",e);
            }
        }
    }

}