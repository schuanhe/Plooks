package com.schuanhe.plooks.service;

import com.schuanhe.plooks.domain.Video;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author ASUS
* @description 针对表【videos(视频表)】的数据库操作Service
* @createDate 2023-08-16 17:25:40
*/
public interface VideoService extends IService<Video> {

    Integer uploadVideoInfo(Video video);

}
