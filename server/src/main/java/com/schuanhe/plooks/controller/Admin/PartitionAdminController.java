package com.schuanhe.plooks.controller.Admin;


import com.schuanhe.plooks.domain.Partition;
import com.schuanhe.plooks.service.Admin.PartitionAdminService;
import com.schuanhe.plooks.utils.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${base-url}/partition/admin")
public class PartitionAdminController {

    @Autowired
    private PartitionAdminService partitionAdminService;

    /**
     * 新增分区
     */
    @PostMapping
    public ResponseResult<?> addPartition(@RequestBody Partition partition) {
        try {
            partitionAdminService.addPartition(partition);
        }catch (Exception e) {
            return ResponseResult.error(e.getMessage());
        }

        return ResponseResult.success();
    }

    /**
     * 删除分区
     */
    @DeleteMapping("/{id}")
    public ResponseResult<?> deletePartition(@PathVariable Integer id) {
        partitionAdminService.deletePartition(id);
        return ResponseResult.success();
    }
}
