package com.schuanhe.plooks.service.Admin.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.schuanhe.plooks.domain.Partition;
import com.schuanhe.plooks.domain.User;
import com.schuanhe.plooks.mapper.PartitionsMapper;
import com.schuanhe.plooks.mapper.UserMapper;
import com.schuanhe.plooks.service.Admin.PartitionAdminService;
import com.schuanhe.plooks.service.User.PartitionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Objects;


@Service
public class PartitionAdminServiceImpl extends ServiceImpl<PartitionsMapper, Partition> implements PartitionAdminService {

    @Autowired
    private PartitionService partitionService;

    @Override
    public void addPartition(Partition partition) throws Exception{
        if (partition.getContent() == null || partition.getContent().equals("")) {
            throw new Exception("参数错误");
        }
        Partition newPartition = new Partition();
        newPartition.setContent(partition.getContent());
        newPartition.setCreatedAt(new Date());
        newPartition.setUpdatedAt(new Date());
        if (partition.getParentId() <= 0) {
            newPartition.setParentId(null);
        }else {
            newPartition.setParentId(partition.getParentId());
        }

        baseMapper.insert(newPartition);
    }

    @Override
    public void deletePartition(Integer id) {
        // 如果删除子分区自动到默认分区（pid = 1）
        List<Integer> subPartitionIds = partitionService.getSubPartitionIds(id);
        if (subPartitionIds.size() == 0)
            return;
        Partition newPartition = new Partition();

        newPartition.setDeletedAt(new Date());
        newPartition.setId(id);
        baseMapper.updateById(newPartition);
        if (subPartitionIds.size() == 1 && Objects.equals(subPartitionIds.get(0), id)) {
            // 如果当前分区没有二级分区则直接删除
        }else {
            // 如果当前分区有二级分区则将其删除
            subPartitionIds.forEach(subPartitionId -> {
                    // 如果当前分区是二级分区则将其父分区设置为默认分区
                    newPartition.setId(subPartitionId);
                    newPartition.setParentId(1);
                    newPartition.setDeletedAt(null);
                baseMapper.updateById(newPartition);
            });
        }


    }
}
