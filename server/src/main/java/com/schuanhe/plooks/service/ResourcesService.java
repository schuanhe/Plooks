package com.schuanhe.plooks.service;

import com.schuanhe.plooks.domain.Resources;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author ASUS
* @description 针对表【resources(资源表)】的数据库操作Service
* @createDate 2023-08-17 18:32:11
*/
public interface ResourcesService extends IService<Resources> {

    void add(Integer vid, String url);
}
