package com.schuanhe.plooks.service.User.impl;

import com.qiniu.http.Response;
import com.schuanhe.plooks.domain.Resources;
import com.schuanhe.plooks.service.User.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;

@Service
public class UploadServiceImpl implements UploadService {

    @Autowired
    private QiniuServiceImpl qiniuService;

    @Autowired
    private ResourcesServiceImpl resourcesService;


    @Override
    public String uploadImage(InputStream inputStream, String path) throws IOException {
        String url = qiniuService.uploadFile(inputStream, path);
        return url;
    }

    @Override
    public void uploadFile(InputStream inputStream, Integer vid,Integer rid) throws Exception {
        // 上传到本地or其他地方

        // 设置文件路径
        String path = "video/" + vid + "/" + System.currentTimeMillis() + ".mp4";

        String url = qiniuService.uploadFile(inputStream, path);

        Resources resources = qiniuService.getVideo(url);

        resources.setId(rid);
        // 保存到数据库
        resources.setStatus(500); //等待审核

        // 保存到数据库
        resourcesService.updateById(resources);
        // 删除redis缓存
        qiniuService.delete(url);
    }


    @Override
    public Response delete(String key) throws IOException {
        return null;
    }

    @Override
    public Resources getVideo(String url) throws Exception {
        return null;
    }
}
