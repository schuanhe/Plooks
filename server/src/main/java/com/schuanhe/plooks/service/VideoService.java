package com.schuanhe.plooks.service;

import com.schuanhe.plooks.domain.Video;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author ASUS
* @description 针对表【videos(视频表)】的数据库操作Service
* @createDate 2023-08-16 17:25:40
*/
public interface VideoService extends IService<Video> {

    /**
     * 上传视频信息
     * @param video 视频信息
     * @return 视频id
     */
    Integer uploadVideoInfo(Video video);

    /**
     * 获取视频信息
     * @param vid 视频id
     * @return 视频信息
     */
    Video getVideoInfo(Integer vid);


}
