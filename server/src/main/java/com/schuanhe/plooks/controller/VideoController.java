package com.schuanhe.plooks.controller;


import com.schuanhe.plooks.domain.Resources;
import com.schuanhe.plooks.domain.User;
import com.schuanhe.plooks.domain.Video;
import com.schuanhe.plooks.service.ResourcesService;
import com.schuanhe.plooks.service.UserService;
import com.schuanhe.plooks.service.VideoService;
import com.schuanhe.plooks.utils.RedisCache;
import com.schuanhe.plooks.utils.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 视频
 */
@RestController
@RequestMapping("${base-url}/video")
public class VideoController {

    @Autowired
    private VideoService videoService;

    @Autowired
    private ResourcesService resourcesService;

    @Autowired
    private UserService userService;

    @Autowired
    private RedisCache redisCache;


    /**
     * 通过id获取视频
     * @return 用户和视频的信息
     */
    @GetMapping("/{vid}")
    public ResponseResult<?> getVideoById(@PathVariable("vid") String vid) {
        Video video = new Video();

        try {
            //将vid转换为int类型，错误则返回
            int vidInt = Integer.parseInt(vid);
            video.setId(vidInt);

        } catch (NumberFormatException e) {
            return ResponseResult.fail("vid格式错误");
        }

        // 获取视频信息
        // 先获取redis中的视频信息

        Video redisVideo = redisCache.getCacheObject("video:info:" + video.getId());

        // 如果redis中有视频信息，则直接返回
        if (redisVideo != null) {
            Map<String, Object> data = new HashMap<>();
            data.put("video", redisVideo);
            return ResponseResult.success(data);
        }

        // 如果redis中没有视频信息，则从数据库中获取
        video = videoService.getById(video);

        // 获取视频资源
        List<Resources> resources = resourcesService.getResourcesByVid(video.getId());

        if (resources == null || resources.size() < 1) {
            return ResponseResult.fail("视频不存在");
        }

        // 将资源添加到视频中
        video.setResources(resources);

        Integer uid = video.getUid();

        if (uid == null) {
            return ResponseResult.fail("视频作者不存在");
        }

        //获取用户信息
        User userInfo = userService.getUserInfoById(uid);

        video.setAuthor(userInfo);

        // 将视频信息添加到redis中
        redisCache.setCacheObject("video:info:" + video.getId(), video);

        Map<String, Object> data = new HashMap<>();
        data.put("video", video);


        return ResponseResult.success(data);
    }



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

    /**
     * 修改视频信息
     */
    @PutMapping("/info")
    public ResponseResult<?> updateVideoInfo(@RequestBody Video video) {
        ResponseResult<String> validationResult = validateVideoInfo(video);
        if (validationResult != null) {
            return validationResult;
        }
        boolean result = videoService.updateById(video);
        if (result) {
            return ResponseResult.success();
        } else {
            return ResponseResult.error("修改失败");
        }
    }

    /**
     * 视频状态
     * @return 视频状态
     */
    @GetMapping("/status/{vid}")
    public ResponseResult<?> videoStatus(@PathVariable String vid){
        // 获取视频信息
        Video videoInfo = videoService.getVideoInfo(Integer.valueOf(vid));
        //通过vid获取相关资源
        List<Resources> resourcesByVid = resourcesService.getResourcesByVid(Integer.valueOf(vid));
        //传入相关资源
        videoInfo.setResources(resourcesByVid);
        // 返回数据
        if (videoInfo.getId() == null || resourcesByVid == null) {
            return ResponseResult.error("视频不存在");
        }

        Map<String, Object> data = new HashMap<>();
        data.put("video", videoInfo);

        return ResponseResult.success(data);
    }

    /**
     * 提交审核
     */
    @PostMapping("/review/{vid}")
    public ResponseResult<?> submitReview(@PathVariable String vid){
        // 判断vid是否合法
        if (vid == null) {
            return ResponseResult.error("视频不存在");
        }
        // 提交审核
        boolean submitReview = videoService.submitReview(Integer.valueOf(vid));
        if (submitReview) {
            return ResponseResult.success("提交成功");
        } else {
            return ResponseResult.error("提交失败");
        }
    }


    /**
     * 稿件管理
     *
     */
    @GetMapping("/upload/{size}/{page}")
    public ResponseResult<?> uploadVideo(@PathVariable Integer size, @PathVariable Integer page){
        // 获取稿件信息
        List<Video> uploadVideos = videoService.getUploadVideo(page, size);
        // 获取稿件总数
        long count = videoService.getUploadVideoCount();

        // 返回数据
        if (uploadVideos == null) {
            return ResponseResult.error("稿件不存在");
        }

        Map<String, Object> data = new HashMap<>();

        data.put("total", count);
        data.put("videos", uploadVideos);


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
        if (video.getPartitionId() == null) {
            return ResponseResult.error("分区ID不能为空");
        }
        return null; // Validation successful
    }

}
