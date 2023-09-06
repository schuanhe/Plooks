package com.schuanhe.plooks.controller;


import com.schuanhe.plooks.domain.Collections;
import com.schuanhe.plooks.service.ArchiveService;
import com.schuanhe.plooks.service.CollectionsService;
import com.schuanhe.plooks.utils.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 收藏夹功能
 */
@RestController
@RequestMapping("${base-url}/collection")
public class CollectionController {

    @Autowired
    private CollectionsService collectionsService;

    /**
     * 新增收藏夹
     */
    @PostMapping
    public ResponseResult<String> addCollection(@RequestBody Map<String,String> data) {
        String name = data.get("name");
        if (name != null && name.length() < 1)
            return ResponseResult.error("收藏夹名字不能为空");
        try {
            collectionsService.addCollection(name);
        }catch (Exception e){
            return ResponseResult.error(e.getMessage());
        }
        return ResponseResult.success("新增成功");
    }

    /**
     * 删除收藏夹
     */
    @DeleteMapping("/{id}")
    public ResponseResult<String> deleteCollection(@PathVariable("id") Integer id) {
        try {
            collectionsService.deleteCollection(id);
        }catch (Exception e){
            return ResponseResult.error(e.getMessage());
        }
        return ResponseResult.success("删除成功");
    }

    /**
     * 修改收藏夹
     */
    @PutMapping
    public ResponseResult<String> updateCollection(@RequestBody Collections info) {
        if (info.getName().length() < 1)
            return ResponseResult.error("收藏夹名字不能为空");

        try {
            collectionsService.updateCollection(info);
        }catch (Exception e){
            return ResponseResult.error(e.getMessage());
        }
        return ResponseResult.success("修改成功");
    }

    /**
     * 获取收藏夹列表
     */
    @GetMapping
    public ResponseResult<?> getCollectionList() {
        List<Collections> list = collectionsService.getCollectionList();

        Map<String, Object> data = new HashMap<>();
        data.put("collections", list);

        return ResponseResult.success(data);
    }

    /**
     * 获取收藏夹详细信息
     */
    @GetMapping("/{id}")
    public ResponseResult<?> getCollectionInfo(@PathVariable("id") Integer id) {
        Collections collection;
        try {
            collection = collectionsService.getById(id);
        }catch (Exception e){
            return ResponseResult.error(e.getMessage());
        }

        Map<String, Object> data = new HashMap<>();
        data.put("collection", collection);

        return ResponseResult.success(data);
    }

}
