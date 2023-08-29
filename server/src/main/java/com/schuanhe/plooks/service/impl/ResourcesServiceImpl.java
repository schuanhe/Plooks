package com.schuanhe.plooks.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.schuanhe.plooks.domain.Resources;
import com.schuanhe.plooks.service.ResourcesService;
import com.schuanhe.plooks.mapper.ResourcesMapper;
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


    @Override
    public List<Resources> getResourcesByVid(Integer vid) {
        // 通过视频vid获取视频资源(可能有多个资源)

        //创建查询条件
         QueryWrapper<Resources> queryWrapper = new QueryWrapper<>();
         queryWrapper.eq("vid", vid);

        return baseMapper.selectList(queryWrapper);
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
        return resources.getId();
    }
}




