package com.schuanhe.plooks.service;

import com.schuanhe.plooks.domain.Resources;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author ASUS
* @description 针对表【resources(资源表)】的数据库操作Service
* @createDate 2023-08-17 18:32:11
*/
public interface ResourcesService extends IService<Resources> {

    /**
     * 根据视频id获取视频资源
     * @param vid 视频id
     * @return 视频资源
     */
    List<Resources> getResourcesByVid(Integer vid);
}
