package com.schuanhe.plooks.controller.User;

import com.schuanhe.plooks.domain.Resources;
import com.schuanhe.plooks.service.User.ResourcesService;
import com.schuanhe.plooks.service.User.UploadService;
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
    private UploadService uploadFile;

    @Autowired
    private ResourcesService resourcesService;

    @PostMapping("/image")
    public ResponseResult<Map<String, String>> uploadImage(@RequestParam("image") MultipartFile image) throws IOException {
        // 设置文件路径

        String path = "image/" + System.currentTimeMillis() + ".jpg";

        String url = uploadFile.uploadImage(image.getInputStream(), path);
        HashMap<String, String> map = new HashMap<>();
        map.put("url", url);
        return ResponseResult.success(map);
    }

    @PostMapping("/video/{vid}")
    public ResponseResult<String> uploadVideo(@RequestParam("video") MultipartFile video, @PathVariable("vid") String vid) throws IOException {
        //通过spring security上下文获取UserDetails
        Integer userId = WebUtils.getUserId();
        if (userId == null) {
            return ResponseResult.fail("请先登录");
        }

        try {
            //将vid转换为int类型，错误则返回
            Integer vidInt = Integer.valueOf(vid);
            //新增资源
            Resources resources = new Resources();
            resources.setVid(vidInt);
            resources.setUid(userId);
            resources.setStatus(200);

            //保存资源并且返回资源id
            Integer rid = resourcesService.saveAndGetId(resources);

            uploadFile.uploadFile(video.getInputStream(), vidInt, rid);

        }catch (NumberFormatException e){
            return ResponseResult.fail("vid格式错误");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.fail("文件解析失败");
        }

        return ResponseResult.success("上传成功");

    }

}
