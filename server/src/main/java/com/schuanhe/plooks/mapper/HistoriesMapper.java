package com.schuanhe.plooks.mapper;

import com.schuanhe.plooks.domain.Histories;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
* @author ASUS
* @description 针对表【histories(历史记录表)】的数据库操作Mapper
* @createDate 2023-08-29 19:37:08
* @Entity com.schuanhe.plooks.domain.Histories
*/
public interface HistoriesMapper extends BaseMapper<Histories> {

    List<Histories> getHistoryList(int i, int size, Integer uid);
}




