package com.schuanhe.plooks.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.schuanhe.plooks.domain.Partition;
import com.schuanhe.plooks.service.PartitionService;
import com.schuanhe.plooks.mapper.PartitionsMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author ASUS
* @description 针对表【partitions(分区表)】的数据库操作Service实现
* @createDate 2023-08-16 16:21:11
*/
@Service
public class PartitionServiceImpl extends ServiceImpl<PartitionsMapper, Partition>
    implements PartitionService {

    @Override
    public List<Partition> partitionList() {
        // 获取分区列表去除删除字段上不为null的数据
        List<Partition> partitionList = this.list();
        partitionList.removeIf(partition -> partition.getDeletedAt() != null);
        // 获取分区列表去除时间字段
        partitionList.forEach(partition -> {
            partition.setCreatedAt(null);
            partition.setUpdatedAt(null);
        });

        return partitionList;

    }
}




