package com.schuanhe.plooks.service.User.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.schuanhe.plooks.domain.Partition;
import com.schuanhe.plooks.service.User.PartitionService;
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

    @Override
    public List<Integer> getSubPartitionIds(Integer pid) {
        List<Integer> pids = new java.util.ArrayList<>();
        // 获取当前分区和父分区为pid的所有二级分区id
        baseMapper.selectList(new QueryWrapper<Partition>().eq("parent_id", pid)).forEach(partition -> {
            if (partition.getDeletedAt() == null || partition.getId() != null) {
                // 如果二级分区未被删除，则将其id加入pids
                pids.add(partition.getId());
            }
        });
        if (pids.size() == 0) {
            // 如果当前分区没有二级分区，则将当前分区id加入pids
            pids.add(pid);
        }

        return pids;
    }
}




