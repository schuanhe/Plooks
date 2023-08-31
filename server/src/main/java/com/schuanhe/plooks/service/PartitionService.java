package com.schuanhe.plooks.service;

import com.schuanhe.plooks.domain.Partition;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author ASUS
* @description 针对表【partitions(分区表)】的数据库操作Service
* @createDate 2023-08-16 16:21:11
*/
public interface PartitionService extends IService<Partition> {

    /**
     * 获取分区列表
     * 去除时间字段和已删除字段
     * @return 分区列表
     */
    List<Partition> partitionList();

    /**
     * 获取当前分区所有二级分区id
     * @return
     */
    List<Integer> getSubPartitionIds(Integer pid);
}
