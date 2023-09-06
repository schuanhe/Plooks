package com.schuanhe.plooks.mapper;

import com.schuanhe.plooks.domain.Collections;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
* @author ASUS
* @description 针对表【collections(收藏夹表)】的数据库操作Mapper
* @createDate 2023-09-06 18:27:04
* @Entity com.schuanhe.plooks.domain.Collections
*/
public interface CollectionsMapper extends BaseMapper<Collections> {

    List<Collections> selectByName(String name, Integer uid);

    /**
     * 根据主键删除
     * 只是设置了deletedAt字段
     * @param id 主键
     */
    void deleteById(Integer id);

}




