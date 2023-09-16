package com.schuanhe.plooks.service.Admin.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.schuanhe.plooks.domain.Resources;
import com.schuanhe.plooks.domain.User;
import com.schuanhe.plooks.domain.Video;
import com.schuanhe.plooks.mapper.VideoMapper;
import com.schuanhe.plooks.service.Admin.VideoAdminService;
import com.schuanhe.plooks.service.User.ResourcesService;
import com.schuanhe.plooks.service.User.UserService;
import com.schuanhe.plooks.utils.RedisCache;
import com.schuanhe.plooks.utils.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;


@Service
public class VideoAdminServiceImpl extends ServiceImpl<VideoMapper, Video> implements VideoAdminService {

    @Autowired
    private UserService userService;

    @Autowired
    private ResourcesService resourcesService;

    @Autowired
    private RedisCache redisCache;

    @Override
    public List<Video> getVideoList(Integer page, Integer size, int pid) {
        if (page == null || page < 1) {
            page = 1;
        }
        if (size == null || size < 1) {
            size = 10;
        }
        QueryWrapper<Video> videoQueryWrapper = new QueryWrapper<>();
        if (pid != 0)
            videoQueryWrapper.eq("partition_id",pid);
        videoQueryWrapper.isNull("deleted_at");
        videoQueryWrapper.last("LIMIT " + (page - 1) * size + "," + size);

        List<Video> videos = baseMapper.selectList(videoQueryWrapper);

        // 为每个视频添加作者信息
        videos.forEach(video -> {
            video.setAuthor(userService.getUserInfoById(video.getUid()));
        });
        return videos;
    }

    @Override
    public Long getVideoCount(int pid) {
        QueryWrapper<Video> videoQueryWrapper = new QueryWrapper<>();
        if (pid != 0)
            videoQueryWrapper.eq("partition_id",pid);
        videoQueryWrapper.isNull("deleted_at");
        return baseMapper.selectCount(videoQueryWrapper);
    }

    @Override
    public List<Video> getReviewVideoList(Integer page, Integer size) {
        QueryWrapper<Video> videoQueryWrapper = new QueryWrapper<>();
        videoQueryWrapper.eq("status",500);
        videoQueryWrapper.isNull("deleted_at");
        videoQueryWrapper.last("LIMIT " + (page - 1) * size + "," + size);
        return baseMapper.selectList(videoQueryWrapper);
    }

    @Override
    public Long getReviewVideoCount() {
        QueryWrapper<Video> videoQueryWrapper = new QueryWrapper<>();
        videoQueryWrapper.eq("status",500);
        videoQueryWrapper.isNull("deleted_at");
        return baseMapper.selectCount(videoQueryWrapper);
    }

    @Override
    public List<Resources> getVideoResource(Integer id) {
        return resourcesService.getAllResourcesByVid(id);
    }

    @Override
    public boolean reviewVideoResource(Resources resources) {
        Resources newResources = new Resources();
        newResources.setId(resources.getId());
        newResources.setStatus(resources.getStatus());
        return resourcesService.updateById(resources);
    }

    @Override
    public boolean reviewVideo(Video video) {
        Video newVideo = new Video();
        newVideo.setId(video.getId());
        newVideo.setStatus(video.getStatus());
        redisCache.deleteObject("video:info:" + video.getId());
        redisCache.deleteObject("video:info:resources:" + video.getId());
        int i = baseMapper.updateById(newVideo);
        return i == 1;
    }

    @Override
    public List<Video> searchVideo(String keyword, Integer page, Integer size) {
        if (page == null || page < 1) {
            page = 1;
        }
        if (size == null || size < 1) {
            size = 10;
        }
        QueryWrapper<Video> videoQueryWrapper = new QueryWrapper<>();
        videoQueryWrapper.like("title",keyword);
        videoQueryWrapper.isNull("deleted_at");
        videoQueryWrapper.last("LIMIT " + (page - 1) * size + "," + size);
        List<Video> videos = baseMapper.selectList(videoQueryWrapper);
        // 设置用户信息
        videos.forEach(video -> {
            video.setAuthor(userService.getUserInfoById(video.getUid()));
        });
        return videos;
    }

    @Override
    public Long searchVideoCount(String keyword) {
        QueryWrapper<Video> videoQueryWrapper = new QueryWrapper<>();
        videoQueryWrapper.like("title",keyword);
        videoQueryWrapper.isNull("deleted_at");
        return baseMapper.selectCount(videoQueryWrapper);
    }

    @Override
    public boolean deleteVideo(Integer vid){
        Video video = new Video();
        video.setId(vid);
        video.setDeletedAt(new Date());
        int i = baseMapper.updateById(video);
        // 删除Redis中的视频信息
        redisCache.deleteObject("video:info:" + vid);
        redisCache.deleteObject("video:info:resources:" + vid );
        redisCache.deleteObject("video:info:author:" + vid);
        redisCache.deleteObject("video:clicks:" + vid);
        return i == 1;
    }
}
