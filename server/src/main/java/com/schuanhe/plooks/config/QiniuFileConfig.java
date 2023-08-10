package com.schuanhe.plooks.config;


import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class QiniuFileConfig {

    @Value("${qiniu.access}")
    private String access;

    @Value("${qiniu.secret}")
    private String secret;



    @Bean
    public com.qiniu.storage.Configuration qiniuConfiguration() {
        //七牛云的配置类，和注解有点冲突
        return new com.qiniu.storage.Configuration(Region.huanan());
    }

    /** * 构建一个七牛云上传工具实例 */
    @Bean
    public UploadManager uploadManager() {
        return new UploadManager(qiniuConfiguration());
    }


    /** * 认证信息实例 * @return */
    @Bean
    public Auth auth() {
        return Auth.create(access, secret);
    }

    /** * 构建七牛空间管理实例 */
    @Bean
    public BucketManager bucketManager() {
        return new BucketManager(auth(), qiniuConfiguration());
    }

}