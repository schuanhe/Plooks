package com.schuanhe.plooks.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.schuanhe.plooks.domain.Danmukus;
import com.schuanhe.plooks.service.DanmukusService;
import com.schuanhe.plooks.mapper.DanmukusMapper;
import com.schuanhe.plooks.utils.RedisCache;
import com.schuanhe.plooks.utils.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.crypto.Data;
import java.util.Date;
import java.util.List;

/**
* @author ASUS
* @description 针对表【danmukus(弹幕表)】的数据库操作Service实现
* @createDate 2023-09-05 13:01:36
*/
@Service
public class DanmukusServiceImpl extends ServiceImpl<DanmukusMapper, Danmukus>
    implements DanmukusService{

    @Autowired
    private RedisCache redisCache;

    @Override
    public List<Danmukus> getDanmukus(Integer vid, Integer part) {
        // 先获取redis中的弹幕
        List<Danmukus> danmukus = redisCache.getCacheList("video:info:danmukus:" + vid + ":" + part);

        // 如果redis中没有弹幕
        if (danmukus.size()==0) {
            // 获取弹幕
            danmukus = baseMapper.selectList(new QueryWrapper<Danmukus>().eq("vid", vid).eq("part", part).isNull("deleted_at"));
            // 将弹幕存入redis
            if (danmukus.size() != 0)
                redisCache.setCacheList("video:info:danmukus:" + vid + ":" + part, danmukus);
        }

        return danmukus;
    }

    @Override
    public void addDanmukus(Danmukus danmukus) {
        if(!isDanmukusKey(danmukus))
            return;
        if (danmukus.getPart() == null)
            danmukus.setPart(1);
        if (danmukus.getMode() == null)
            danmukus.setMode(0);

        danmukus.setUid(WebUtils.getUserId());
        Date date = new Date();
        danmukus.setCreatedAt(date);
        danmukus.setUpdatedAt(date);
        // 保存弹幕
        baseMapper.insert(danmukus);
        // 将弹幕存入redis
        redisCache.setCacheList("video:info:danmukus:" + danmukus.getVid() + ":" + danmukus.getPart(), danmukus);
    }

    // 判断弹幕关键信息
    public boolean isDanmukusKey(Danmukus danmukus) {
        return danmukus.getVid() != null && danmukus.getPart() != null && danmukus.getTime() != null && danmukus.getText() != null;
    }
}




