package com.nannan.batis;

import com.nannan.jdbc.core.JdbcTemplate;
import com.nannan.jdbc.core.PrepareStatementCallBack;

public interface SqlSession {
    void setJdbcTemplate(JdbcTemplate jdbcTemplate);
    void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory);
    Object selectOne(String sqlid, Object[] args, PrepareStatementCallBack pstmtcallback);
}
