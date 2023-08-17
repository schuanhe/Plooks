package com.schuanhe.plooks.controller;


import com.schuanhe.plooks.domain.Video;
import com.schuanhe.plooks.service.VideoService;
import com.schuanhe.plooks.utils.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 视频
 */
@RestController
@RequestMapping("${base-url}/video")
public class VideoController {

    @Autowired
    private VideoService videoService;


    /**
     * 新增视频 上传视频消息
     */
    @PostMapping("/info")
    public ResponseResult<?> uploadVideoInfo(@RequestBody Video video) {
        ResponseResult<String> validationResult = validateVideoInfo(video);
        if (validationResult != null) {
            return validationResult;
        }
        Integer vid = videoService.uploadVideoInfo(video);
        Map<String, Integer> data = new HashMap<>();
        data.put("vid", vid);
        return ResponseResult.success(data);
    }


    //视频消息校验
    private ResponseResult<String> validateVideoInfo(Video video) {
        if (video == null) {
            return ResponseResult.error("视频信息不能为空");
        }
        if (!StringUtils.hasLength(video.getTitle())) {
            return ResponseResult.error("视频标题不能为空");
        }
        if (!StringUtils.hasLength(video.getCover())) {
            return ResponseResult.error("视频封面不能为空");
        }
        if (video.getCopyright() == null) {
            return ResponseResult.error("视频版权不能为空");
        }
        if (video.getPartition() == null) {
            return ResponseResult.error("分区ID不能为空");
        }
        return null; // Validation successful
    }

}
