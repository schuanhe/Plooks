package com.schuanhe.plooks.controller;


import com.schuanhe.plooks.domain.Whispers;
import com.schuanhe.plooks.service.WhispersService;
import com.schuanhe.plooks.utils.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 发送消息
 */
@RestController
@RequestMapping("${base-url}/whisper")
public class WhispersController {

    @Autowired
    private WhispersService whispersService;

    /**
     * 获取消息列表
     */
    @GetMapping
    public ResponseResult<?> getWhispers() {
        List<Whispers> whispers =  whispersService.getWhispers();

        Map<String, Object> data = new HashMap<>();
        data.put("messages", whispers);

        return ResponseResult.success(data);
    }

    /**
     * 获取消息详情
     */
    @GetMapping("/{fid}/{size}/{page}")
    public ResponseResult<?> getWhisperDetail(@PathVariable Integer fid, @PathVariable Integer size, @PathVariable Integer page) {
        List<Whispers> whispersList = whispersService.getWhisperInfo(fid, size, page);
        Map<String, Object> data = new HashMap<>();
        data.put("messages", whispersList);
        return ResponseResult.success(data);
    }

    /**
     * 私信已读
     */
    @PutMapping("/{fid}")
    public ResponseResult<?> readWhisper(@PathVariable Integer fid) {
        whispersService.readWhisper(fid);
        return ResponseResult.success();
    }

    /**
     * 发送私信
     */
    @PostMapping
    public ResponseResult<?> sendWhisper(@RequestBody Whispers whispers) {
        whispersService.sendWhisper(whispers);
        return ResponseResult.success();
    }




}
