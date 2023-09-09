package com.schuanhe.plooks.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.schuanhe.plooks.domain.Archive;
import com.schuanhe.plooks.domain.Message;

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

    List<Archive.Collect> selectCollect(Integer uid, int vid);

    void cancelCollect(Integer uid, int vid, List<Integer> cancelList);

    void addCollect(Integer uid, int vid, List<Integer> addList);

    List<Integer> getCollectVideoIds(Integer cid, Integer uid, int i, Integer size);

    Integer getCollectVideoCount(Integer cid, Integer uid);

    // 防止依赖注入时出错
    Integer selectUidByVid(int vid);

    void insertLikeMessage(Message.LikeMessages likeMessages);
}
