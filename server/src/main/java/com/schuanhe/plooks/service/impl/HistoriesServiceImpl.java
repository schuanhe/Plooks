package com.schuanhe.plooks.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.schuanhe.plooks.domain.Histories;
import com.schuanhe.plooks.service.HistoriesService;
import com.schuanhe.plooks.mapper.HistoriesMapper;
import com.schuanhe.plooks.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
* @author ASUS
* @description 针对表【histories(历史记录表)】的数据库操作Service实现
* @createDate 2023-08-29 19:37:08
*/
@Service
public class HistoriesServiceImpl extends ServiceImpl<HistoriesMapper, Histories>
    implements HistoriesService{

    @Autowired
    private VideoService videoService;

    @Override
    public Histories getProgressByVid(int vid, Integer uid) {
        // 通过视频vid和用户id获取视频播放进度
        QueryWrapper<Histories> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("vid",vid);
        queryWrapper.eq("uid",uid);
        Histories histories = baseMapper.selectOne(queryWrapper);

        if (histories == null) {
            histories = new Histories();
            histories.setTime(0.0);
            histories.setPart(1);
        }
        return histories;

    }

    @Override
    public void addHistory(Histories histories) {
        Date date = new Date();
        histories.setCreatedAt(date);
        histories.setUpdatedAt(date);
        // 添加历史记录
        baseMapper.insert(histories);
    }

    @Override
    public List<Histories> getHistoryList(int size, int page, Integer uid) {
        // 获取历史记录列表


        List<Histories> historyList = baseMapper.getHistoryList((page - 1) * size, size, uid);
        historyList.forEach(histories -> {
            histories.setVideo(videoService.getVideoInfo(histories.getVid()));
        });

        return historyList;
    }
}




