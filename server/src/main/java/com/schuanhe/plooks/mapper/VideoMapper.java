package com.schuanhe.plooks.mapper;

import com.schuanhe.plooks.domain.Video;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
* @author ASUS
* @description 针对表【videos(视频表)】的数据库操作Mapper
* @createDate 2023-08-16 17:25:40
* @Entity com.schuanhe.plooks.domain.Video
*/
public interface VideoMapper extends BaseMapper<Video> {

    void insertVideo(Video video);

    Video selectById(Integer vid);


    List<Video> selectVideoByUid(Integer uid, int i, Integer size);
}




