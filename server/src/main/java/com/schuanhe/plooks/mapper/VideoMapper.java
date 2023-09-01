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

    List<Video> selectVideoByUidAll(Integer uid);

    /**
     * 获取所有子分区的正常视频
     * @return 上传视频总数
     */
    List<Video> selectGoodVideoListByPartitionIds(List<Integer> partitions, int i, Integer size);

    List<Video> selectGoodVideoList(int i, Integer size);

    int selectGoodVideoCountByPartitionIds(List<Integer> partitions);

    int selectGoodVideoCount();

    List<Video> selectRecommendVideo(Integer size);

    Integer selectVideoCountByUid(Integer uid);
}




