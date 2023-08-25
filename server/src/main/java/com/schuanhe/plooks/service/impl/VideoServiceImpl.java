package com.schuanhe.plooks.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.schuanhe.plooks.domain.Video;
import com.schuanhe.plooks.domain.model.UserDetailsImpl;
import com.schuanhe.plooks.service.VideoService;
import com.schuanhe.plooks.mapper.VideoMapper;
import com.schuanhe.plooks.utils.WebUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;

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
        video.setStatus(100);

        baseMapper.insertVideo(video);

        return video.getId();
    }

    @Override
    public Video getVideoInfo(Integer vid) {
        // 获取视频信息
        return baseMapper.selectById(vid);
    }

}




