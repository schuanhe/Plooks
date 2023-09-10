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
        if(uid != 0 ) {
            this.uid = uid;
        } else{
            log.info("[链接失败]token={} socket={}",token,this);
            onClose();
            return;
        }
        WebSocketManager.whispers.put(uid,session);
        log.info("[链接成功]uid={} socket={}",uid,this);
        System.out.println("uid = " + uid);
    }

    @OnClose
    public void onClose() {
        // 断开连接删除用户删除session
        log.info("[断开连接]token={} session={} socket={}",token,session,this);

    }

    @OnMessage
    public void onMessage(String message) {
        log.info("[收到消息]token={} session={} socket={} message={}",token,session,this,message);

    }

    //发送消息
    public void sendMassageList(Map<String, Object> sendMsg){
    }

    //发送消息
    public void sendMassage(String name,String message){}


    // 此为广播消息
//    public void sendAllMessage(String message) {
//        for(WhisperWebSocket webSocket : webSockets) {
//            System.out.println("【websocket消息】广播消息:"+message);
//            try {
//                webSocket.session.getAsyncRemote().sendText(message);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }

    // 此为单点消息 (发送文本)
//    public void sendTextMessage(Integer userId, String message) {
//        Session session = (Session)CurPool.sessionPool.get(userId).get(1);
//        if (session != null) {
//            try {
//                session.getAsyncRemote().sendText(message);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }

    // 此为单点消息 (发送对象)
//    public void sendObjMessage(String sessionId, Object message) {
//        Session session = CurPool.sessionPool.get(sessionId);
//        if (session != null) {
//            try {
////                session.getAsyncRemote().sendObject(message);
//                session.getBasicRemote().sendText(JsonUtils.objectToJson(message));
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }

}