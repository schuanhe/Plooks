package com.schuanhe.plooks.controller;

import com.schuanhe.plooks.service.QiniuService;
import com.schuanhe.plooks.utils.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
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
    private QiniuService qiniuService;

    @PostMapping("/image")
    public ResponseResult<Map<String, String>> uploadImage(@RequestParam("image") MultipartFile image) throws IOException {
        String url = qiniuService.uploadFile(image.getInputStream());
        HashMap<String, String> map = new HashMap<>();
        map.put("url", url);
        return ResponseResult.success(map);
    }

}