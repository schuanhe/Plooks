package com.schuanhe.plooks.service.User.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Client;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.storage.persistent.FileRecorder;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;
import com.schuanhe.plooks.domain.Resources;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.Date;


@Slf4j
@Service
public class QiniuServiceImpl implements InitializingBean {

    @Autowired
    private UploadManager uploadManager;
    @Autowired
    private BucketManager bucketManager;

    @Autowired
    private Client client;


    @Autowired
    private Auth auth;

    @Value("${qiniu.path}")
    private String url;

    @Value("${qiniu.bucket}")
    private String bucket;


    // 定义七牛云上传的相关策略
    private StringMap putPolicy;


    public String uploadFile(InputStream inputStream, String path) throws IOException {
        Response response = this.uploadManager.put(inputStream, path, getUploadToken(), null, null);
        //如果失败重试3次
        int retry = 0;
        while (response.needRetry() && retry < 3) {
            response = this.uploadManager.put(inputStream, null, getUploadToken(), null, null);
            retry++;
        }
        //解析结果
        System.out.println(response);
        DefaultPutRet putRet = JSON.parseObject(response.bodyString(), DefaultPutRet.class);
        String return_path = url + "/" + putRet.key;
        log.info("文件名称={}", return_path);
        return return_path;

    }

    public String uploadFileS(InputStream inputStream, String path) throws IOException {
        //构造一个带指定 Region 对象的配置类
        Configuration cfg = new Configuration(Region.huanan());
        cfg.resumableUploadAPIVersion = Configuration.ResumableUploadAPIVersion.V2;// 指定分片上传版本
        cfg.resumableUploadMaxConcurrentTaskCount = 2;  // 设置分片上传并发，1：采用同步上传；大于1：采用并发上传


        //设置断点续传文件进度保存目录
        String localTempDir = Paths.get(System.getenv("java.io.tmpdir"), bucket).toString();
        File tempDirFile = new File(localTempDir);
        if (!tempDirFile.exists()) {
            tempDirFile.mkdirs();
        }
        try {
            //设置断点续传文件进度保存目录
            FileRecorder fileRecorder = new FileRecorder(localTempDir);
            UploadManager uploadManager = new UploadManager(cfg, fileRecorder);
            try {
                Response response = uploadManager.put(path, null, getUploadToken());
                //解析上传成功的结果
                DefaultPutRet putRet = JSON.parseObject(response.bodyString(), DefaultPutRet.class);
                String return_path = url + "/" + putRet.key;
                log.info("文件名称={}", return_path);
                return return_path;

            } catch (QiniuException ex) {
                Response r = ex.response;
                System.err.println(r.toString());
                try {
                    System.err.println(r.bodyString());
                } catch (QiniuException ex2) {
                    //ignore
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }


    public Response delete(String key) throws QiniuException {
        Response response = this.bucketManager.delete(bucket, key);
        int retry = 0;
        while (response.needRetry() && retry++ < 3) {
            response = this.bucketManager.delete(bucket, key);
        }
        return response;
    }

    public Resources getVideo(String url) throws Exception {
        // 新文件信息
        Resources resources = new Resources();
        //拼接请求地址
        String requestUrl = url + "?avinfo";

        Response response = client.get(requestUrl, null);
        //解析为json对象
        JSONObject jsonObject = JSON.parseObject(response.bodyString());
        //获取视频时长
        double doubleValue = jsonObject.getJSONObject("format").getDoubleValue("duration");
        // 设置视频上传时间
        Date time = new Date();

        resources.setDuration(doubleValue);
        resources.setCreatedAt(time);
        resources.setUrl(url);

        return resources;
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        this.putPolicy = new StringMap();
        putPolicy.put("returnBody", "{\"key\":\"$(key)\",\"hash\":\"$(etag)\",\"bucket\":\"$(bucket)\",\"width\":$(imageInfo.width), \"height\":${imageInfo.height}}");
        // 自定义文件名字
        // putPolicy.put("saveKey", UUID.randomUUID().timestamp());
    }


    /**
     * 获取上传凭证 * * @return
     */
    private String getUploadToken() {
        return this.auth.uploadToken(bucket, null, 3600, putPolicy);
    }
}