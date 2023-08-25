package com.schuanhe.plooks.controller;

import com.schuanhe.plooks.domain.Resources;
import com.schuanhe.plooks.domain.model.UserDetailsImpl;
import com.schuanhe.plooks.service.ResourcesService;
import com.schuanhe.plooks.service.UploadService;
import com.schuanhe.plooks.utils.CoreUtils;
import com.schuanhe.plooks.utils.JwtUtil;
import com.schuanhe.plooks.utils.ResponseResult;
import com.schuanhe.plooks.utils.WebUtils;
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
        //通过spring security上下文获取UserDetails
        Integer userId = WebUtils.getUserId();
        if (userId == null) {
            return ResponseResult.fail("请先登录");
        }
        // 设置文件路径
        String path = "video/" + vid + System.currentTimeMillis() + ".mp4";
        Resources resources;
        try {
            String url = qiniuService.uploadFile(video.getInputStream(), path);
            resources = qiniuService.getVideo(url);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseResult.fail("上传失败");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.fail("文件解析失败");
        }

        // 保存到数据库
        resources.setStatus(0);
        resources.setVid(vid);
        resources.setUid(userId);

        // 保存到数据库
        boolean save = resourcesService.save(resources);
        if (!save) {
            return ResponseResult.fail("上传失败");
        }

        return ResponseResult.success("上传成功");

    }

}
