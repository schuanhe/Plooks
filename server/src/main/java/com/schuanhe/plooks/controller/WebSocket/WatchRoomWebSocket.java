package com.schuanhe.plooks.controller.WebSocket;


import com.alibaba.fastjson.JSON;
import com.schuanhe.plooks.domain.form.RoomFrom;
import com.schuanhe.plooks.utils.JwtUtil;
import com.schuanhe.plooks.utils.WebSocketManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;


@Slf4j
@Component
@ServerEndpoint("/api/v1/websocket/watchroom/{rid}/{token}")
//此注解相当于设置访问URL
public class WatchRoomWebSocket {
    /**
     *  与某个客户端的连接对话，需要通过它来给客户端发送消息
     */
    public Session session;

    public Integer rid;

    public Integer uid;

    // 关闭理由
    public String reason = "链接关闭";


    @OnOpen
    public void onOpen(Session session, @PathParam(value="rid")Integer rid, @PathParam(value="token")String token) {
        this.session = session;
        this.rid = rid;
        Integer uid = JwtUtil.getUseridNoException(token);
        if(uid == 0 ) {
            log.info("[链接失败]token={} token不正确",token);
            onClose();
            return;
        }
        this.uid = uid;
        RoomFrom roomFrom = WebSocketManager.WatchroomOnline.get(rid);
        // 判断是否在房间里
        if(roomFrom == null || !roomFrom.getUserIds().contains(uid)) {
            log.info("[链接失败]rid={} uid={} 你还没进入该房间",rid,uid);
            onClose();
            return;
        }
        // 加入链接数据
        WebSocketManager.WatchroomOnline.get(rid).getUserSession().put(uid,session);
        log.info("[链接成功]rid={} uid={} 当前房间人数={}",rid,uid,WebSocketManager.WatchroomOnline.get(rid).getUserSession().size());
    }

    @OnClose
    public void onClose() {
        try {
            // 设置关闭理由reason
            CloseReason customCloseReason = new CloseReason(CloseReason.CloseCodes.NORMAL_CLOSURE, reason);
            // 去除用户数据
            WebSocketManager.WatchroomOnline.get(rid).getUserSession().remove(uid);
            WebSocketManager.WatchroomOnline.get(rid).getUserIds().remove(uid);
            session.close(customCloseReason);
        } catch (Exception e) {
            log.error("[断开连接]rid={} uid={} 当前房间人数={}",rid,uid,WebSocketManager.WatchroomOnline.get(rid).getUserSession().size());
        }
        log.info("[链接关闭]rid={} uid={} 当前房间人数={}",rid,uid,WebSocketManager.WatchroomOnline.get(rid).getUserSession().size());
    }

    @OnMessage
    public void onMessage(String message) {
        sendMessage(rid,uid,message);
        log.info("[收到消息] rid={} uid={} message={}",rid,uid,message);
    }


    public static void sendMessage(Integer rid, Integer uid, String message) {
        WebSocketManager.WatchroomOnline.get(rid).getUserSession().forEach((k,v)->{
            System.out.println("k:"+k+"uid:"+uid);
            if (!k.equals(uid)) {
                v.getAsyncRemote().sendText(message);
            }
        });
        log.info("[发送消息]rid={} uid={} message={} size={}",rid,uid,message,WebSocketManager.WatchroomOnline.get(rid).getUserSession().size() - 1);
    }

    public static <T> void sendObject(Integer rid, Integer uid, T message) {
        WebSocketManager.WatchroomOnline.get(rid).getUserSession().forEach((k,v)->{
            if (!k.equals(uid)) {

                String encode = Base64.getEncoder().encodeToString(JSON.toJSONString(message).getBytes());
                v.getAsyncRemote().sendText(encode);
            }
        });
        log.info("[发送消息]rid={} uid={} message={}",rid,uid,message);
    }

}