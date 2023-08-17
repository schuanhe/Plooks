package com.schuanhe.plooks.controller;

import com.schuanhe.plooks.domain.Resources;
import com.schuanhe.plooks.service.ResourcesService;
import com.schuanhe.plooks.service.UploadService;
import com.schuanhe.plooks.service.VideoService;
import com.schuanhe.plooks.utils.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 文件上传
 */
@RestController
@RequestMapping("${base-url}/upload")
public class UploadController {

    @Autowired
    private UploadService qiniuService;

    @Autowired
    private ResourcesService resourcesService;

    @PostMapping("/image")
    public ResponseResult<Map<String, String>> uploadImage(@RequestParam("image") MultipartFile image) throws IOException {
        // 设置文件路径

        String path = "image/" + System.currentTimeMillis() + ".jpg";

        String url = qiniuService.uploadFile(image.getInputStream(), path);
        HashMap<String, String> map = new HashMap<>();
        map.put("url", url);
        return ResponseResult.success(map);
    }

    @PostMapping("/video/{vid}")
    public ResponseResult<String> uploadVideo(@RequestParam("video") MultipartFile video, @PathVariable("vid") Integer vid) throws IOException {
        String path = "video/" + vid + ".mp4";
        String url = qiniuService.uploadFile(video.getInputStream(), path);
        // 获取视频时长
        //上传视频后，将视频的url保存到数据库中



        return ResponseResult.success(url);
    }

}
