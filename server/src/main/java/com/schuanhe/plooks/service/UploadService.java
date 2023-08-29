package com.schuanhe.plooks.service;

import com.qiniu.http.Response;
import com.schuanhe.plooks.domain.Resources;
import org.springframework.scheduling.annotation.Async;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public interface UploadService {

    /**
     * 上传头像
     *
     * @return 文件路径
     * @throws IOException 上传失败抛出异常
     */
    String uploadImage(InputStream inputStream, String path) throws IOException;

    /**
     * 上传视频
     * @param inputStream 视频流
     * @param vid 视频id
     * @param rid 资源id
     * @throws Exception 上传失败抛出异常
     */
    @Async
    void uploadFile(InputStream inputStream, Integer vid, Integer rid) throws Exception;

    /**
     * 删除文件
     *
     * @param key 文件名
     * @return 删除结果
     * @throws IOException 删除失败抛出异常
     */
    Response delete(String key) throws IOException;


    /**
     * 通过地址获取视信息
     */
    Resources getVideo(String url) throws Exception;




}