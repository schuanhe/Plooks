package com.schuanhe.plooks.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.schuanhe.plooks.domain.Partitions;
import com.schuanhe.plooks.service.PartitionsService;
import com.schuanhe.plooks.utils.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * 分区
 */
@RestController
@RequestMapping("${base-url}/partition")
public class PartitionController {

    @Autowired
    private PartitionsService partitionsService;

    /**
     * 获取分区列表
     */
    @GetMapping
    public ResponseResult<List<Partitions>> getPartitionList() {
        // 获取分区列表(去除时间字段和已删除字段)
        List<Partitions> partitionList = partitionsService.partitionList();
        return ResponseResult.success(partitionList);
    }

}
