package com.schuanhe.plooks.controller;


import com.schuanhe.plooks.domain.Partition;
import com.schuanhe.plooks.service.PartitionService;
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
    private PartitionService partitionService;

    /**
     * 获取分区列表
     */
    @GetMapping
    public ResponseResult<List<Partition>> getPartitionList() {
        // 获取分区列表(去除时间字段和已删除字段)
        List<Partition> partitionList = partitionService.partitionList();
        return ResponseResult.success(partitionList);
    }

}
