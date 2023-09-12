package com.schuanhe.plooks.service.Admin.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.schuanhe.plooks.domain.Carousels;
import com.schuanhe.plooks.mapper.CarouselsMapper;
import com.schuanhe.plooks.service.Admin.CarouselAdminService;
import com.schuanhe.plooks.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class CarouselAdminServiceImpl extends ServiceImpl<CarouselsMapper, Carousels> implements CarouselAdminService {

    @Autowired
    private RedisCache redisCache;

    @Override
    public void addCarousel(Carousels carousels) {
        carousels.setCreatedAt(new Date());
        carousels.setUpdatedAt(new Date());

        baseMapper.insert(carousels);
        // 删除redis
        redisCache.deleteObject("carousels");
        redisCache.deleteObject("refresh:carousel");
    }

    @Override
    public void deleteCarousel(Integer id) {
        baseMapper.deleteById(id);
        // 删除redis
        redisCache.deleteObject("carousels");
        redisCache.deleteObject("refresh:carousel");
    }


}
