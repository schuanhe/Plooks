package com.schuanhe.plooks.service;

import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;

import java.io.File;
import java.io.InputStream;

public interface QiniuService {

    /**
     * 上传文件
     * @param file 文件
     * @return 文件路径
     * @throws QiniuException 上传失败抛出异常
     */
    String uploadFile(File file) throws QiniuException;

    /**
     * 上传文件
     * @param inputStream 文件流
     * @return 文件路径
     * @throws QiniuException 上传失败抛出异常
     */
    String uploadFile(InputStream inputStream) throws QiniuException;

    /**
     * 删除文件
     * @param key 文件名
     * @return 删除结果
     * @throws QiniuException 删除失败抛出异常
     */
    Response delete(String key) throws QiniuException;
}