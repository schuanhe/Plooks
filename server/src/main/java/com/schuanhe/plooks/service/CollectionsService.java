package com.schuanhe.plooks.service;

import com.schuanhe.plooks.domain.Collections;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author ASUS
* @description 针对表【collections(收藏夹表)】的数据库操作Service
* @createDate 2023-09-06 18:27:04
*/
public interface CollectionsService extends IService<Collections> {

    void addCollection(String name) throws Exception;

    void deleteCollection(Integer id) throws Exception;

    void updateCollection(Collections info) throws Exception;

    List<Collections> getCollectionList();

    Collections getById(Integer id) throws Exception;
}
