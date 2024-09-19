package com.nannan.batis;

public interface SqlSessionFactory {
    SqlSession openSqlSession();
    MapperNode getMapperNode(String name);
}
