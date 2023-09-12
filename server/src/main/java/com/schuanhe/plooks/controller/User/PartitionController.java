package com.schuanhe.plooks.controller.User;


import com.schuanhe.plooks.domain.Partition;
import com.schuanhe.plooks.service.User.PartitionService;
import com.schuanhe.plooks.utils.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


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
    public ResponseResult<Map<String,List<Partition>>> getPartitionList() {
        // 获取分区列表(去除时间字段和已删除字段)
        List<Partition> partitionList = partitionService.partitionList();

        Map<String,List<Partition>> data = new HashMap<>();
        data.put("partitions",partitionList);

        return ResponseResult.success(data);
    }

}
