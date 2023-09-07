package com.schuanhe.plooks.controller;

import com.schuanhe.plooks.service.ArchiveService;
import com.schuanhe.plooks.utils.ResponseResult;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 点赞收藏接口
 */
@RestController
@RequestMapping("${base-url}/archive")
public class ArchiveController {

    @Autowired
    private ArchiveService archiveService;

    /**
     * 获取点赞收藏数据
     */
    @GetMapping("/{vid}")
    public ResponseResult<?> getArchive(@PathVariable String vid) {
        int vidInt;
        try {
            vidInt = Integer.parseInt(vid);
        }catch (NumberFormatException e){
            return ResponseResult.error("参数错误");
        }
        Map<String,Integer> stat = archiveService.getArchive(vidInt);

        Map<String, Object> data = new HashMap<>();

        data.put("stat", stat);

        return ResponseResult.success(data);

    }

    /**
     * 获取是否点赞
     */
    @GetMapping("/has/like/{vid}")
    public ResponseResult<?> getHasLike(@PathVariable String vid) {
        int vidInt;
        try {
            vidInt = Integer.parseInt(vid);
        }catch (NumberFormatException e){
            return ResponseResult.error("参数错误");
        }
        Map<String, Boolean> data = new HashMap<>();
        data.put("like", archiveService.hasLike(vidInt));
        return ResponseResult.success(data);
    }


    /**
     * 点赞
     */
    @PostMapping("/like/{vid}")
    public ResponseResult<?> like(@PathVariable String vid) {
        int vidInt;
        try {
            vidInt = Integer.parseInt(vid);
        }catch (NumberFormatException e){
            return ResponseResult.error("参数错误");
        }
        archiveService.addLike(vidInt);
        return ResponseResult.success();
    }

    /**
     * 取消点赞
     */
    @DeleteMapping("/like/{vid}")
    public ResponseResult<?> cancelLike(@PathVariable String vid) {
        int vidInt;
        try {
            vidInt = Integer.parseInt(vid);
        }catch (NumberFormatException e){
            return ResponseResult.error("参数错误");
        }
        archiveService.cancelLike(vidInt);
        return ResponseResult.success();
    }

    /**
     * 获取是否收藏
     */
    @GetMapping("/has/collect/{vid}")
    public ResponseResult<?> getHasCollect(@PathVariable String vid) {
        int vidInt;
        try {
            vidInt = Integer.parseInt(vid);
        }catch (NumberFormatException e){
            return ResponseResult.error("参数错误");
        }
        Map<String, Boolean> data = new HashMap<>();
        data.put("collect", archiveService.hasCollect(vidInt));
        return ResponseResult.success(data);
    }

    /**
     * 获取该视频的收藏夹
     */
    @GetMapping("/collect/{vid}")
    public ResponseResult<?> getCollect(@PathVariable String vid) {
        int vidInt;
        try {
            vidInt = Integer.parseInt(vid);
        }catch (NumberFormatException e){
            return ResponseResult.error("参数错误");
        }
        Map<String, Object> data = new HashMap<>();
        data.put("collectionIds", archiveService.getCollectListId(vidInt));
        return ResponseResult.success(data);
    }

    /**
     * 收藏
     */
    @PostMapping("/collect")
    public ResponseResult<?> collect(@RequestBody addCollect addCollect) {

        try {
            archiveService.addCollect(addCollect.getVid(),addCollect.getAddList(),addCollect.getCancelList());
        }catch (Exception e){
            return ResponseResult.error(e.getMessage());
        }
        return ResponseResult.success();
    }

    /**
     * 收藏信息
     */
    @Data
    static class addCollect {
        private int vid;
        private List<Integer> addList;
        private List<Integer> cancelList;
    }

}


