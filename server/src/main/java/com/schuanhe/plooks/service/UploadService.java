package com.schuanhe.plooks.service;

import com.qiniu.http.Response;
import com.schuanhe.plooks.domain.Resources;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public interface UploadService {

    /**
     * 上传文件
     * @param file 文件
     * @return 文件路径
     * @throws IOException 上传失败抛出异常
     */
    String uploadFile(File file, String path) throws IOException;

    /**
     * 上传文件
     * @param inputStream 文件流
     * @param path 文件路径
     * @return 文件路径
     * @throws IOException 上传失败抛出异常
     */
    String uploadFile(InputStream inputStream, String path) throws IOException;

    /**
     * 断点续传
     */
    String uploadFileS(InputStream inputStream, String path) throws IOException;

    /**
     * 删除文件
     * @param key 文件名
     * @return 删除结果
     * @throws IOException 删除失败抛出异常
     */
    Response delete(String key) throws IOException;

}