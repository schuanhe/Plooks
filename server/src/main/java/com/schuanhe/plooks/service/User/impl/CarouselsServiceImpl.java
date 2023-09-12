package com.schuanhe.plooks.service.User.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.schuanhe.plooks.domain.Carousels;
import com.schuanhe.plooks.service.User.CarouselsService;
import com.schuanhe.plooks.mapper.CarouselsMapper;
import com.schuanhe.plooks.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author ASUS
* @description 针对表【carousels(轮播图表)】的数据库操作Service实现
* @createDate 2023-08-31 19:32:07
*/
@Service
public class CarouselsServiceImpl extends ServiceImpl<CarouselsMapper, Carousels>
    implements CarouselsService{

    @Autowired
    private RedisCache redisCache;

    @Override
    public List<Carousels> getCarousels() {
        // 先获取redis中的轮播图
        List<Carousels> carousels = redisCache.getCacheList("carousels",0,-1);
        // 如果redis中没有轮播图，就从数据库中获取
        // 获取轮播图3分钟内是否刷新
        Object cacheObject = redisCache.getCacheObject("refresh:carousel");
        // 根据缓存对象是否为null来设置refresh变量
        boolean refresh = cacheObject != null && (boolean) cacheObject;

        if (carousels.size() == 0 && !refresh){
            // 从数据库中获取轮播图
            List<Carousels> newCarousels = baseMapper.selectList(null);
            // 将轮播图存入redis
            if (newCarousels != null && newCarousels.size() > 0){
                redisCache.setCacheList("carousels",newCarousels);
                // 从redis中获取轮播图
                carousels = redisCache.getCacheList("carousels",0,-1);
                // 设置轮播图3分钟内不刷新
                redisCache.setCacheObject("refresh:carousel",true,60*3);
            }
        }
        return carousels;
    }
}




