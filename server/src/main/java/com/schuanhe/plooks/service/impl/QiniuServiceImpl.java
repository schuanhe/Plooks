package com.schuanhe.plooks.service.impl;

import com.alibaba.fastjson.JSON;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;
import com.schuanhe.plooks.service.QiniuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.InputStream;


@Slf4j
@Service
public class QiniuServiceImpl implements QiniuService, InitializingBean {

    @Autowired
    private UploadManager uploadManager;
    @Autowired
    private BucketManager bucketManager;
    @Autowired
    private Auth auth;

    @Value("${qiniu.path}")
    private String path;

    @Value("${qiniu.bucket}")
    private String bucket;


    // 定义七牛云上传的相关策略
    private StringMap putPolicy;


    @Override
    public String uploadFile(File file) throws QiniuException {
        Response response = this.uploadManager.put(file, null, getUploadToken());
        int retry = 0;
        while (response.needRetry() && retry < 3) { 
            response = this.uploadManager.put(file, null, getUploadToken());
            retry++;
        }
        //解析结果
        DefaultPutRet putRet = JSON.parseObject(response.bodyString(), DefaultPutRet.class);
        String return_path = path + "/" + putRet.key;
        log.info("文件名称={}", return_path);
        return return_path;
    }


    /** * 以流的形式上传 * * @param inputStream * @return * @throws QiniuException */
    @Override
    public String uploadFile(InputStream inputStream) throws QiniuException {
        Response response = this.uploadManager.put(inputStream, null, getUploadToken(), null, null);
        int retry = 0;
        while (response.needRetry() && retry < 3) { 
            response = this.uploadManager.put(inputStream, null, getUploadToken(), null, null);
            retry++;
        }
        //解析结果
        DefaultPutRet putRet = JSON.parseObject(response.bodyString(), DefaultPutRet.class);
        String return_path = path + "/" + putRet.key;
        log.info("文件名称={}", return_path);
        return return_path;
    }
    /** * 删除七牛云上的相关文件 * * @param key * @return * @throws QiniuException */
    @Override
    public Response delete(String key) throws QiniuException { 
        Response response = bucketManager.delete(bucket, key);
        int retry = 0;
        while (response.needRetry() && retry++ < 3) { 
            response = bucketManager.delete(bucket, key);
        }
        return response;
    }
    @Override
    public void afterPropertiesSet() throws Exception { 
        this.putPolicy = new StringMap();
        putPolicy.put("returnBody", "{\"key\":\"$(key)\",\"hash\":\"$(etag)\",\"bucket\":\"$(bucket)\",\"width\":$(imageInfo.width), \"height\":${imageInfo.height}}");
        // 自定义文件名字
        // putPolicy.put("saveKey", UUID.randomUUID().timestamp());
    }


    /** * 获取上传凭证 * * @return */
    private String getUploadToken() { 
        return this.auth.uploadToken(bucket, null, 3600, putPolicy);
    }
}