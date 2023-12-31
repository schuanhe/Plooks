package com.schuanhe.plooks.service.User;

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
     * 根据视频id获取视频资源(已经审核通过的资源)
     * @param vid 视频id
     * @return 视频资源
     */
    List<Resources> getResourcesByVid(Integer vid);

    /**
     * 根据视频id获取视频资源(所有资源)
     */
    List<Resources> getAllResourcesByVid(Integer vid);


    /**
     * 根据视频id删除视频资源
     * @param vid 视频id
     * @return 是否删除成功
     */
    boolean deleteResource(Integer vid);


    Integer saveAndGetId(Resources resources);

    /**
     * 根据id更新视频资源
     * @param resources 视频资源
     */
    boolean updateById(Resources resources);

    boolean updateResourceTitle(Resources resources);
}
