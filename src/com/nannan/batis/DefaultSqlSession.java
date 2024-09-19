package com.nannan.batis;

import com.nannan.jdbc.core.JdbcTemplate;
import com.nannan.jdbc.core.PrepareStatementCallBack;
import lombok.Getter;

public class DefaultSqlSession implements SqlSession{

    @Getter
    JdbcTemplate jdbcTemplate;
    @Getter
    SqlSessionFactory sqlSessionFactory;

    @Override
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }


    @Override
    public Object selectOne(String sqlid, Object[] args, PrepareStatementCallBack pstmtcallback) {
        System.out.println(sqlid);
        String sql = this.sqlSessionFactory.getMapperNode(sqlid).getSql();
        System.out.println(sql);

        return jdbcTemplate.query(sql, args, pstmtcallback);
    }
}
