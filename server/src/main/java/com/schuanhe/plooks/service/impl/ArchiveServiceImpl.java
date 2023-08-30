package com.schuanhe.plooks.service.impl;

import com.baomidou.mybatisplus.core.conditions.AbstractWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.schuanhe.plooks.domain.Archive;
import com.schuanhe.plooks.mapper.ArchiveMapper;
import com.schuanhe.plooks.service.ArchiveService;
import com.schuanhe.plooks.utils.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
}
