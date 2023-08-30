package com.schuanhe.plooks.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.schuanhe.plooks.domain.Archive;

import java.util.List;

public interface ArchiveMapper extends BaseMapper<Archive>{

    /**
     * 获取收藏数
     * @param vid 视频id
     * @return 收藏数
     */
    int getCollectCount(int vid);

    /**
     * 获取点赞数
     * @param vid 视频id
     * @return 点赞数
     */
    int getLikeCount(int vid);

    int hasLike(int vid, int uid);

    int hasCollect(int vid, Integer uid);

    Archive.Like hasLikeNoDelete(int vid, Integer uid);

    void updateLikeById(Archive.Like like);

    void insertLike(Archive.Like like);
}
