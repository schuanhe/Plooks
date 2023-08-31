package com.schuanhe.plooks.service;

import com.schuanhe.plooks.domain.User;
import com.schuanhe.plooks.domain.Video;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

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


    boolean submitReview(Integer vid);

    List<Video> getUploadVideo(Integer page, Integer size);

    /**
     * 获取当前登录用户上传视频总数
     * @return 上传视频总数
     */
    Long getUploadVideoCount();

    boolean updateVideoInfoById(Video video);

    /**
     * 获取所有正常的视频
     * @param partition 分区
     * @param page 页码
     * @param size 每页数量
     * @return 视频列表
     */
    List<Video> getGoodVideoList(Integer partition, Integer size, Integer page);

    int getGoodVideoCount(Integer partition);

    List<Video> getRecommendVideo(Integer size);
}
