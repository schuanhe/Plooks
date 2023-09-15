package com.schuanhe.plooks.service.User.impl;

import com.schuanhe.plooks.domain.form.RoomFrom;
import com.schuanhe.plooks.service.User.WatchroomService;
import com.schuanhe.plooks.utils.WebSocketManager;
import com.schuanhe.plooks.utils.WebUtils;
import org.springframework.stereotype.Service;

import javax.websocket.Session;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class WatchroomServiceImpl implements WatchroomService {
    @Override
    public void enterRoom(Integer rid, String password) throws Exception {

        Integer userId = WebUtils.getUserId();
        if (userId == null)
            throw new Exception("用户未登录");

        Map<Integer, RoomFrom> watchroomOnline = WebSocketManager.WatchroomOnline;
        // 先判断房间是否存在
        if (watchroomOnline.get(rid) == null) {
            throw new Exception("房间不存在");
        }
        RoomFrom roomFrom = watchroomOnline.get(rid);
        // 判断是否有密码
        if (roomFrom.getIsPublic()){
            // 不需要密码，将用户存入预备信息中
            WebSocketManager.WatchroomOnline.get(rid).getUserIds().add(userId);
            return;
        }
        // 判断密码是否正确
        if (roomFrom.getPassword().equals(password)){
            // 密码正确，进入房间
            WebSocketManager.WatchroomOnline.get(rid).getUserIds().add(userId);
            return;
        }
        // 都没完成，抛出密码错误异常
        throw new RuntimeException("密码不正确");

    }

    @Override
    public Integer createRoom(RoomFrom roomFrom) throws RuntimeException {
        // 判断信息是否完整
        if (roomFrom.getIsPublic() == null||roomFrom.getPid() == null||roomFrom.getVid() == null)
            throw new RuntimeException("信息不完整");
        // 判断是否有密码
        if (roomFrom.getIsPublic() && roomFrom.getPassword() == null)
            throw new RuntimeException("非公开房间必须设置密码");
        // 判断创建者
        Integer userId = WebUtils.getUserId();
        if (userId == null)
            throw new RuntimeException("用户未登录");
        RoomFrom newRoomFrom = new RoomFrom();

        newRoomFrom.setIsPlay(false);
        newRoomFrom.setTime(0.0);
        newRoomFrom.setUserSession(new HashMap<>());
        newRoomFrom.setAdminId(userId);
        newRoomFrom.setIsPublic(roomFrom.getIsPublic());
        newRoomFrom.setPassword(roomFrom.getPassword());
        newRoomFrom.setVid(roomFrom.getVid());
        newRoomFrom.setPid(roomFrom.getPid());
        List<Integer> ids = new ArrayList<>();
        ids.add(userId);
        newRoomFrom.setUserIds(ids);
        newRoomFrom.setId(WebSocketManager.WatchroomOnline.size()+1);
        // 将房间信息存入在线房间中
        WebSocketManager.WatchroomOnline.put(newRoomFrom.getId(),newRoomFrom);
        return newRoomFrom.getId();
    }

    @Override
    public RoomFrom getRoomInfo(Integer rid) throws Exception{
        RoomFrom roomFrom = WebSocketManager.WatchroomOnline.get(rid);
        // 判断房间是否存在
        if (roomFrom == null)
            throw new Exception("房间不存在");
        // 判断用户是否登录
        Integer userId = WebUtils.getUserId();
        if (userId == null)
            throw new Exception("用户未登录");
        // 判断房间是在房间
        if (!roomFrom.getUserIds().contains(userId)){
            if (roomFrom.getIsPublic())
                throw new Exception("用户暂时没进入该私密房间");
            WebSocketManager.WatchroomOnline.get(rid).getUserIds().add(userId);
        }
        return WebSocketManager.WatchroomOnline.get(rid);
    }

    @Override
    public void updateRoom(Integer rid, RoomFrom roomFrom) {
        Integer userId = WebUtils.getUserId();
        if (userId == null)
            throw new RuntimeException("用户未登录");
        WebSocketManager.WatchroomOnline.get(rid).setPid(roomFrom.getPid());
    }
}
