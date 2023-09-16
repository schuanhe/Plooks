package com.schuanhe.plooks.mapper;

import com.schuanhe.plooks.utils.SqlProvider;
import org.apache.ibatis.annotations.SelectProvider;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;


@Component
public interface AiMangeMapper {

    @SelectProvider(type = SqlProvider.class, method = "dynamicSql")
    List<Map<String,Object>> findByCriteria(String sql);
}
