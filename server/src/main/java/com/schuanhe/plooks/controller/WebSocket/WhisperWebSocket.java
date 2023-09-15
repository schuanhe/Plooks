package com.schuanhe.plooks.controller.WebSocket;


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
import java.util.Map;


@Slf4j
@Component
@ServerEndpoint("/api/v1/websocket/whisper/{token}")
//此注解相当于设置访问URL
public class WhisperWebSocket {


    /**
     *  与某个客户端的连接对话，需要通过它来给客户端发送消息
     */
    public Session session;
    /**
     * 标识当前连接客户端的用户名
     */
    public Integer uid;

    public String token;


    @OnOpen
    public void onOpen(Session session, @PathParam(value="token")String token) {
        this.session = session;
        this.token = token;

        // 将token 解密
        Integer uid = JwtUtil.getUseridNoException(token);
        if(uid == 0 ) {
            log.info("[链接失败]token={} token不正确",token);
            onClose();
            return;
        }
        WebSocketManager.whispers.put(uid,session);
        log.info("[链接成功]uid={} socket={}",uid,this);
        System.out.println("uid = " + uid);
    }

    @OnClose
    public void onClose() {
        try {
            WebSocketManager.whispers.remove(uid);
        }catch (Exception e) {
            log.error("[断开连接]uid={} 当前链接数{}",uid,WebSocketManager.whispers.size());
        }
        log.info("[断开连接]uid={} 当前链接数{}",uid,WebSocketManager.whispers.size());
    }

    @OnMessage
    public void onMessage(String message) {
        log.info("[收到消息]token={} session={} socket={} message={}",token,session,this,message);
    }

}