package com.schuanhe.plooks.controller.User;


import com.schuanhe.plooks.domain.form.RoomFrom;
import com.schuanhe.plooks.service.User.WatchroomService;
import com.schuanhe.plooks.utils.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("${base-url}/watchroom")
public class WatchroomController {

    @Autowired
    private WatchroomService watchroomService;


    /**
     * 进入房间
     */
    @PostMapping("/{rid}")
    public ResponseResult<?> enterRoom(@PathVariable("rid") Integer rid, @RequestBody Map<String, String> map) {
        String password = map.get("password");
        try {
            watchroomService.enterRoom(rid, password);
        }catch (RuntimeException e){
            return ResponseResult.error(5002,e.getMessage());
        } catch (Exception e){
            return ResponseResult.error(e.getMessage());
        }

        return ResponseResult.success();
    }

    /**
     * 创建房间
     */
    @PostMapping
    public ResponseResult<?> createRoom(@RequestBody RoomFrom roomFrom) {
        Integer rid = watchroomService.createRoom(roomFrom);
        Map<String, Object> data = new HashMap<>();
        data.put("rid", rid);
        return ResponseResult.success(data);
    }

    /**
     * 获取房间 信息
     */
    @GetMapping("/{rid}")
    public ResponseResult<?> getRoomInfo(@PathVariable("rid") Integer rid) {
        RoomFrom roomFrom;
        try {
            roomFrom = watchroomService.getRoomInfo(rid);
        }catch (Exception e){
            return ResponseResult.error(e.getMessage());
        }
        Map<String, Object> data = new HashMap<>();
        data.put("roomInfo", roomFrom);
        return ResponseResult.success(data);
    }

    /**
     * 更新房间
     */
    @PutMapping("/{rid}")
    public ResponseResult<?> updateRoom(@PathVariable("rid") Integer rid, @RequestBody RoomFrom roomFrom) {
        try {
            watchroomService.updateRoom(rid, roomFrom);
        }catch (Exception e){
            return ResponseResult.error(e.getMessage());
        }
        return ResponseResult.success();
    }

}
