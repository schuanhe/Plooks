package com.schuanhe.plooks.service.User;

import com.schuanhe.plooks.domain.form.RoomFrom;

import java.util.Map;

public interface WatchroomService {
    void enterRoom(Integer rid, String password) throws Exception;

    Integer createRoom(RoomFrom roomFrom) throws RuntimeException;

    RoomFrom getRoomInfo(Integer rid) throws Exception;

    void updateRoom(Integer rid, RoomFrom roomFrom);
}
