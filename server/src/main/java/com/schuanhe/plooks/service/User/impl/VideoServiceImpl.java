package com.schuanhe.plooks.service.User.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.schuanhe.plooks.domain.Resources;
import com.schuanhe.plooks.domain.User;
import com.schuanhe.plooks.domain.Video;
import com.schuanhe.plooks.mapper.VideoMapper;
import com.schuanhe.plooks.service.User.*;
import com.schuanhe.plooks.utils.RedisCache;
import com.schuanhe.plooks.utils.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private PartitionService partitionService;

    @Autowired
    private ResourcesService resourcesService;

    // 点赞与收藏的服务
    @Autowired
    private ArchiveService archiveService;

    @Autowired
    private UserService userService;

    @Value("${plooks.video.cache.expire}")
    private int videoCacheExpire;    // 视频列表缓存过期时间


    @Override
    public Integer uploadVideoInfo(Video video) {

        //获取当前登录用户id
        Integer id = WebUtils.getUserId();

        //设置视频作者id
        video.setUid(id);
        video.setId(null);

        //设置创建时间
        video.setCreatedAt(new Date());
        video.setStatus(100);

        baseMapper.insertVideo(video);

        return video.getId();
    }

    @Override
    public Video getVideoInfo(Integer vid) {

        // 先获取redis中的视频信息
        Video video = redisCache.getCacheObject("video:info:" + vid);

        // 如果redis中没有视频信息
        if (video == null) {
            // 获取视频信息
            video = baseMapper.selectById(vid);
            // 将视频信息存入redis
            redisCache.setCacheObject("video:info:" + vid, video);
        }

        // 获取视频信息
        return video;
    }

    @Override
    public boolean submitReview(Integer vid) {
        // 提交审核
        Video video = new Video();
        video.setId(vid);
        video.setStatus(500);
        if (baseMapper.updateById(video) > 0){
            //删除redis中的视频信息
            redisCache.deleteObject("video:info:" + vid);
            return true;
        }
        return false;
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

    @Override
    public boolean updateVideoInfoById(Video video) {
        // 更新视频信息
        //删除redis中的视频信息
        redisCache.deleteObject("video:info:" + video.getId());
        return baseMapper.updateById(video) > 0;
    }

    @Override
    public List<Video> getGoodVideoList(Integer partition, Integer size, Integer page) {

        // 先获取redis中的视频信息
        List<Video> videos = redisCache.getCacheObject("video:good:list:" + partition + ":" + size + ":" + page);
        if (videos != null) {
            return videos;
        }

        // 判断分区
        if (partition == 0) {
            // 获取所有视频
            videos = baseMapper.selectGoodVideoList((page - 1) * size, size);
        } else {
            // 获取指定分区的视频
            // 判断分区是否是子分区
            List<Integer> partitions = partitionService.getSubPartitionIds(partition);
            if (partitions == null) {
                // 如果不是子分区
                List<Integer> partitions1 = new java.util.ArrayList<>();
                partitions1.add(partition);
                partitions = partitions1;
            }
            // 获取所有分区的视频
            videos = baseMapper.selectGoodVideoListByPartitionIds(partitions, (page - 1) * size, size);
        }
        // 将视频信息存入redis
        videos.forEach(video -> {
            // 获取视频作者信息
            video.setAuthor(userService.getUserInfoById(video.getUid()));
        });

        redisCache.setCacheObject("video:good:list:" + partition + ":" + size + ":" + page, videos, videoCacheExpire);
        return videos;
    }

    @Override
    public int getGoodVideoCount(Integer partition) {
        // 先获取redis中的视频总数
        Integer count = redisCache.getCacheObject("video:good:count:" + partition);
        if (count != null) {
            return count;
        }
        // 判断分区
        if (partition == 0) {
            // 获取所有视频总数
            count = baseMapper.selectGoodVideoCount();
        } else {
            // 获取指定分区的视频总数
            // 判断分区是否是子分区
            List<Integer> partitions = partitionService.getSubPartitionIds(partition);
            if (partitions == null) {
                // 如果不是子分区
                List<Integer> partitions1 = new java.util.ArrayList<>();
                partitions1.add(partition);
                partitions = partitions1;
            }
            // 获取所有分区的视频总数
            count = baseMapper.selectGoodVideoCountByPartitionIds(partitions);
        }
        // 将视频总数存入redis
        redisCache.setCacheObject("video:good:count:" + partition, count, videoCacheExpire);
        return count;
    }

    @Override
    public List<Video> getRecommendVideo(Integer size) {
        // 通过redis获取推荐视频
        List<Video> videos = redisCache.getCacheObject("video:good:recommend:list:" + size);
        if (videos != null) {
            return videos;
        }
        // 按照播放量排序获取视频
        videos = baseMapper.selectRecommendVideo(size);
        // 将视频信息存入redis
        videos.forEach(video -> {
            // 获取视频作者信息
            video.setAuthor(userService.getUserInfoById(video.getUid()));
        });
        return videos;
    }

    @Override
    public List<Video> getVideoListByUid(Integer uid, Integer size, Integer page) {
        // 先获取redis中的视频信息
        List<Video> videos = redisCache.getCacheList("video:list:" + uid, (page - 1) * size, page * size - 1);
        if (videos.size() == 0) {
            // 获取指定用户的视频
            videos = baseMapper.selectVideoByUidAll(uid);
            //获取视频作者信息
            //videos.forEach(video -> {
            //    video.setAuthor(userService.getUserInfoById(video.getUid()));
            //});

            if (videos.size()>0)
                redisCache.setCacheList("video:list:" + uid, videos);
            videos = redisCache.getCacheList("video:list:" + uid, (page - 1) * size, page * size - 1);
        }

        return videos;
    }

    @Override
    public List<Video> getCollectVideo(Integer cid, Integer size, Integer page) {
        // 不使用redis了
        Integer uid = WebUtils.getUserId();
        // 先获取收藏夹的视频id
        List<Integer> vids = archiveService.getCollectVideoIds(cid, uid, size, page);
        if (vids.size() == 0) {
            return new java.util.ArrayList<>();
        }else {
            // 获取视频信息
            return baseMapper.selectBatchIds(vids);
        }
    }

    @Override
    public int getCollectVideoCount(Integer cid) {
        // 不使用redis了
        Integer uid = WebUtils.getUserId();
        // 先获取收藏夹的视频id
        return archiveService.getCollectVideoCount(cid, uid);
    }

    @Override
    public Integer getUidByVid(Integer vid) {
        // 先获取redis中的视频作者id
        Integer uid = redisCache.getCacheObject("video:uid:" + vid);
        if (uid == null) {
            // 获取指定视频的作者id
            uid = baseMapper.selectUidByVid(vid);
            // 将视频作者id存入redis
            redisCache.setCacheObject("video:uid:" + vid, uid);
        }
        return uid;
    }

    @Override
    public Video getVideoById(Integer vid) {



        Video video = this.getVideoInfo(vid);
        // 获取视频资源
        List<Resources> resources = resourcesService.getResourcesByVid(video.getId());

        if (resources.size() < 1) {
            return video;
        }

        // 将资源添加到视频中
        video.setResources(resources);

        Integer uid = video.getUid();

        if (uid == null) {
            return video;
        }

        //获取用户信息
        User userInfo = userService.getUserInfoById(uid);
        video.setAuthor(userInfo);

        // 视频播放量+1
        // 添加视频观看次数Redis缓存
        if (redisCache.hasKey("video:clicks:" + vid)) {
            redisCache.increment("video:clicks:" + vid, 1L);
            // 同步到数据库，60秒内不重复同步
            Object refObject = redisCache.getCacheObject("refresh:video:clicks:" + vid);
            if (refObject == null || !(boolean) refObject) {
                // 视频播放量+1
                Video video1 = new Video();
                video1.setId(vid);
                video1.setClicks(redisCache.getCacheObject("video:clicks:" + vid));
                baseMapper.updateById(video1);
                // 设置刷新缓存标志
                redisCache.setCacheObject("refresh:video:clicks:" + vid, true, 60 );
                // 清除视频info缓存
                redisCache.deleteObject("video:info:" + vid);
            }
        } else {
            redisCache.setCacheObject("video:clicks:" + vid, video.getClicks() + 1);
        }


        return video;

    }

    @Override
    public int getVideoCountByUid(Integer uid) {
        // 先获取redis中的视频总数
        Integer count = redisCache.getCacheObject("video:count:" + uid);
        if (count == null || count == 0) {
            // 获取指定用户的视频总数
            count = baseMapper.selectVideoCountByUid(uid);
            // 将视频总数存入redis
            redisCache.setCacheObject("video:count:" + uid, count);
        }
        return count;
    }

}




