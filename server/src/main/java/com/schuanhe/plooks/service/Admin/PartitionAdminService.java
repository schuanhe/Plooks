package com.schuanhe.plooks.service.Admin;

import com.baomidou.mybatisplus.extension.service.IService;
import com.schuanhe.plooks.domain.Carousels;
import com.schuanhe.plooks.domain.Partition;

public interface PartitionAdminService extends IService<Partition> {
    void addPartition(Partition partition) throws Exception;

    void deletePartition(Integer id);
}
