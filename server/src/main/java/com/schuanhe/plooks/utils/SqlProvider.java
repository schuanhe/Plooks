package com.schuanhe.plooks.utils;

import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.jdbc.SQL;

public class SqlProvider {
    public String dynamicSql(String sql) {
        return sql;
    }
}
