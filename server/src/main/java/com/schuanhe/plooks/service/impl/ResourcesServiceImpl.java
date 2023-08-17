package com.schuanhe.plooks.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.schuanhe.plooks.domain.Resources;
import com.schuanhe.plooks.service.ResourcesService;
import com.schuanhe.plooks.mapper.ResourcesMapper;
import org.springframework.stereotype.Service;

/**
* @author ASUS
* @description 针对表【resources(资源表)】的数据库操作Service实现
* @createDate 2023-08-17 18:32:11
*/
@Service
public class ResourcesServiceImpl extends ServiceImpl<ResourcesMapper, Resources>
    implements ResourcesService{

    @Override
    public void add(Integer vid, String url) {
        Resources resources = new Resources();
        resources.setVid(vid);

        baseMapper.insert(resources);
    }
}




