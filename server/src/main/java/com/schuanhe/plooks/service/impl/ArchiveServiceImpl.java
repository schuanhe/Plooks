package com.schuanhe.plooks.service.impl;

import com.baomidou.mybatisplus.core.conditions.AbstractWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.schuanhe.plooks.domain.Archive;
import com.schuanhe.plooks.mapper.ArchiveMapper;
import com.schuanhe.plooks.service.ArchiveService;
import com.schuanhe.plooks.utils.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ArchiveServiceImpl extends ServiceImpl<ArchiveMapper, Archive> implements ArchiveService {


    @Override
    public Map<String, Integer> getArchive(int vid) {

        HashMap<String, Integer> stat = new HashMap<>();
        stat.put("collect", baseMapper.getCollectCount(vid));
        stat.put("like", baseMapper.getLikeCount(vid));

        return stat;
    }

    @Override
    public Boolean hasLike(int vid) {
        Integer uid = WebUtils.getUserId();
        if (uid == null) {
            return false;
        }
        return baseMapper.hasLike(vid,uid) > 0;
    }

    @Override
    public Boolean hasCollect(int vid) {
        Integer uid = WebUtils.getUserId();
        if (uid == null) {
            return false;
        }
        return baseMapper.hasCollect(vid,uid) > 0;
    }

    @Override
    public void addLike(int vid) {
        Integer uid = WebUtils.getUserId();
        if (uid == null) {
            return;
        }
        //先判断是否已经点赞(不用判断是否删除)
        Archive.Like like = baseMapper.hasLikeNoDelete(vid, uid);
        if (like != null) {
            //已经点赞(不排除已经删除的)
            if (like.getDeletedAt() != null) {
                //已经点赞但已经删除
                //更新时间,删除时间置空
                like.setDeletedAt(null);
                like.setUpdatedAt(new Date());
                baseMapper.updateLikeById(like);
            }
        }else {
            //当前时间
            Date time = new Date();

            //插入数据
            Archive.Like nLike = new Archive.Like();
            nLike.setUid(uid);
            nLike.setVid(vid);
            nLike.setCreatedAt(time);
            nLike.setUpdatedAt(time);
            baseMapper.insertLike(nLike);
        }


    }

    @Override
    public void cancelLike(int vid) {
        Integer uid = WebUtils.getUserId();
        if (uid == null) {
            return;
        }
        //先判断是否已经点赞(不用判断是否删除)
        Archive.Like like = baseMapper.hasLikeNoDelete(vid, uid);
        if (like != null) {
            //已经点赞(不排除已经删除的)
            if (like.getDeletedAt() == null) {
                //已经点赞但未删除
                //更新时间,删除时间
                like.setDeletedAt(new Date());
                like.setUpdatedAt(new Date());
                baseMapper.updateLikeById(like);
            }
        }

    }

    @Override
    public List<Integer> getCollectListId(int vidInt) {
        Integer uid = WebUtils.getUserId();
        List<Integer> collectListId = new ArrayList<>();
        if (uid == null) {
            return collectListId;
        }

        List<Archive.Collect> collects = baseMapper.selectCollect(uid, vidInt);

        for (Archive.Collect collect : collects) {
            collectListId.add(collect.getId());
        }
        return collectListId;
    }

    @Override
    public void addCollect(int vid, List<Integer> addList, List<Integer> cancelList) throws Exception {
        Integer uid = WebUtils.getUserId();
        if (uid == null) {
            throw new Exception("用户未登录");
        }
        //先处理取消收藏
        if (cancelList != null && cancelList.size() > 0) {
            baseMapper.cancelCollect(uid, vid, cancelList);
        }
        //再处理添加收藏
        if (addList != null && addList.size() > 0) {
            baseMapper.addCollect(uid, vid, addList);
        }
    }


    @Override
    public List<Integer> getCollectVideoIds(Integer cid, Integer uid, Integer size, Integer page) {
        return baseMapper.getCollectVideoIds(cid, uid, (page - 1) * size, size);
    }

    @Override
    public Integer getCollectVideoCount(Integer cid, Integer uid) {
        return baseMapper.getCollectVideoCount(cid, uid);
    }


}
