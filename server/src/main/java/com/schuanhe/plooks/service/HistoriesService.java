package com.schuanhe.plooks.service;

import com.schuanhe.plooks.domain.Histories;
import com.baomidou.mybatisplus.extension.service.IService;
import com.schuanhe.plooks.utils.ResponseResult;

/**
* @author ASUS
* @description 针对表【histories(历史记录表)】的数据库操作Service
* @createDate 2023-08-29 19:37:08
*/
public interface HistoriesService extends IService<Histories> {

    Histories getProgressByVid(int vidInt, Integer userId);
}
