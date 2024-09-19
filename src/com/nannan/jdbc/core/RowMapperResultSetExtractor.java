package com.nannan.jdbc.core;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RowMapperResultSetExtractor <T> implements ResultSetExtractor<List<T>>{
    private final RowMapper<T> rowMapper;

    public RowMapperResultSetExtractor(RowMapper<T> rowMapper) {
        this.rowMapper = rowMapper;
    }

    @Override
    public List<T> extractData(ResultSet resultSet) throws SQLException {
        List<T> results = new ArrayList<>();

        int rowNum = 0;
        while(resultSet.next()){
            results.add(this.rowMapper.mapRom(resultSet, rowNum++));
        }
        return results;
    }
}
