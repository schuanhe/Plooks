package com.schuanhe.plooks.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.schuanhe.plooks.domain.Video;
import com.schuanhe.plooks.service.VideoService;
import com.schuanhe.plooks.mapper.VideoMapper;
import com.schuanhe.plooks.utils.WebUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
* @author ASUS
* @description 针对表【videos(视频表)】的数据库操作Service实现
* @createDate 2023-08-16 17:25:40
*/
@Service
public class VideoServiceImpl extends ServiceImpl<VideoMapper, Video>
    implements VideoService {

    @Override
    public Integer uploadVideoInfo(Video video) {

        //获取当前登录用户id
        Integer id = WebUtils.getUserId();

        //设置视频作者id
        video.setUid(id);
        video.setId(null);

        //设置创建时间
        video.setCreatedAt(new Date());
        video.setStatus(300);

        baseMapper.insertVideo(video);

        return video.getId();
    }

    @Override
    public Video getVideoInfo(Integer vid) {
        // 获取视频信息
        return baseMapper.selectById(vid);
    }

    @Override
    public boolean submitReview(Integer vid) {
        // 提交审核
        Video video = new Video();
        video.setId(vid);
        video.setStatus(500);
        return baseMapper.updateById(video) > 0;
    }

    @Override
    public List<Video> getUploadVideo(Integer page, Integer size) {
        // 获取当前登录用户id
        Integer uid = WebUtils.getUserId();

        // 获取当前登录用户上传的视频
        // 通过uid获取视频信息
        return baseMapper.selectVideoByUid(uid, (page - 1) * size, size);
    }

    @Override
    public Long getUploadVideoCount() {
        // 获取当前登录用户id
        Integer uid = WebUtils.getUserId();
        QueryWrapper<Video> wrapper = new QueryWrapper<>();
        // 获取当前登录用户上传的视频总数
        wrapper.eq("uid", uid);

        return baseMapper.selectCount(wrapper);
    }

}




