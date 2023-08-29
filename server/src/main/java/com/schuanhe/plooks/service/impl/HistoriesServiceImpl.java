package com.schuanhe.plooks.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.schuanhe.plooks.domain.Histories;
import com.schuanhe.plooks.service.HistoriesService;
import com.schuanhe.plooks.mapper.HistoriesMapper;
import org.springframework.stereotype.Service;

/**
* @author ASUS
* @description 针对表【histories(历史记录表)】的数据库操作Service实现
* @createDate 2023-08-29 19:37:08
*/
@Service
public class HistoriesServiceImpl extends ServiceImpl<HistoriesMapper, Histories>
    implements HistoriesService{

    @Override
    public Histories getProgressByVid(int vidInt, Integer userId) {
        // 通过视频vid和用户id获取视频播放进度
        QueryWrapper<Histories> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("vid",vidInt);
        queryWrapper.eq("uid",userId);
        Histories histories = baseMapper.selectOne(queryWrapper);

        if (histories == null) {
            histories = new Histories();
            histories.setTime(0.0);
            histories.setPart(1);
        }
        return histories;

    }
}




