package com.schuanhe.plooks.service.User.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.schuanhe.plooks.domain.Resources;
import com.schuanhe.plooks.service.User.ResourcesService;
import com.schuanhe.plooks.mapper.ResourcesMapper;
import com.schuanhe.plooks.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author ASUS
* @description 针对表【resources(资源表)】的数据库操作Service实现
* @createDate 2023-08-17 18:32:11
*/
@Service
public class ResourcesServiceImpl extends ServiceImpl<ResourcesMapper, Resources>
    implements ResourcesService{

    @Autowired
    private RedisCache redisCache;

    @Override
    public List<Resources> getResourcesByVid(Integer vid) {
        // 通过视频vid获取视频资源(可能有多个资源)

        // 先获取redis中的视频资源
        List<Resources> resources = redisCache.getCacheList("video:info:resources:" + vid);

        // 如果redis中没有视频资源
        if (resources.size()==0) {
            // 获取视频资源
            resources = baseMapper.selectList(new QueryWrapper<Resources>().eq("vid", vid).eq("status",0));
            // 将视频资源存入redis
            redisCache.setCacheList("video:info:resources:" + vid, resources);
        }

        return resources;
    }

    @Override
    public List<Resources> getAllResourcesByVid(Integer vid) {
        return baseMapper.selectList(new QueryWrapper<Resources>().eq("vid", vid));
    }


    @Override
    public boolean deleteResource(Integer id) {
        // 删除视频资源
        return baseMapper.deleteById(id) > 0;
    }

    @Override
    public Integer saveAndGetId(Resources resources) {
        // 保存视频资源
        baseMapper.insert(resources);
        if (resources.getId() == null) {
            return 0;
        }
        // 将视频资源存入redis
        redisCache.setCacheList("video:info:resources:" + resources.getVid(), resources);
        return resources.getId();
    }

    @Override
    public boolean updateById(Resources resources) {
        // 更新视频资源
        if (baseMapper.updateById(resources) == 0) {
            return false;
        }
        // 将视频资源缓存删除
        redisCache.deleteObject("video:info:resources:" + resources.getVid());
        return true;
    }

    @Override
    public boolean updateResourceTitle(Resources resources) {

        Resources newResources = new Resources();
        newResources.setId(resources.getId());
        newResources.setTitle(resources.getTitle());
        // 更新视频资源标题
        if (baseMapper.updateById(newResources) == 0) {
            return false;
        }
        // 将视频资源缓存删除
        redisCache.deleteObject("video:info:resources:" + resources.getVid());
        return true;
    }
}




