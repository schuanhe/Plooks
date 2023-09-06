package com.schuanhe.plooks.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.schuanhe.plooks.domain.Collections;
import com.schuanhe.plooks.service.CollectionsService;
import com.schuanhe.plooks.mapper.CollectionsMapper;
import com.schuanhe.plooks.utils.WebUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
* @author ASUS
* @description 针对表【collections(收藏夹表)】的数据库操作Service实现
* @createDate 2023-09-06 18:27:04
*/
@Service
public class CollectionsServiceImpl extends ServiceImpl<CollectionsMapper, Collections>
    implements CollectionsService{



    @Override
    public void addCollection(String name) throws Exception {
        Integer userId = WebUtils.getUserId();
        if (userId == null) {
            throw new Exception("请先登录");
        }
        //判断是否已经存在(list)为了防止藏数据
        List<Collections> collections = baseMapper.selectByName(name, userId);
        if (collections.size() > 0 ) {
            throw new Exception("已经存在");
        }

        Collections collection = new Collections();
        Date time = new Date();
        collection.setName(name);
        collection.setUid(userId);
        collection.setCreatedAt(time);
        collection.setUpdatedAt(time);

        baseMapper.insert(collection);
    }

    @Override
    public void deleteCollection(Integer id) throws Exception{
        Integer userId = WebUtils.getUserId();
        if (userId == null) {
            throw new Exception("请先登录");
        }
        baseMapper.deleteById(id);
    }

    @Override
    public void updateCollection(Collections info) throws Exception {
        Integer userId = WebUtils.getUserId();
        if (userId == null) {
            throw new Exception("请先登录");
        }
        List<Collections> collection = baseMapper.selectByName(info.getName(), userId);
        if (collection == null) {
            throw new Exception("收藏夹不存在");
        }
        info.setId(collection.get(0).getId()); //设置id
        info.setUpdatedAt(new Date());  //设置更新时间

        baseMapper.updateById(info);
    }

    @Override
    public List<Collections> getCollectionList() {
        Integer userId = WebUtils.getUserId();
        if (userId == null) {
            return new ArrayList<>();
        }
        return baseMapper.selectList(new QueryWrapper<Collections>().eq("uid", userId).isNull("deleted_at"));
    }

    @Override
    public Collections getById(Integer id) throws Exception {
        Integer userId = WebUtils.getUserId();
        if (userId == null) {
            throw new Exception("请先登录");
        }

        Collections collections = baseMapper.selectById(id);
        if (!userId.equals(collections.getUid())) {
            //不是自己的收藏夹
            if (!collections.isOpen()) {
                //不是公开的
                throw new Exception("收藏夹不存在");
            }
        }
        return collections;
    }

}




