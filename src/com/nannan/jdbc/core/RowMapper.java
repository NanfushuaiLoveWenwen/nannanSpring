package com.nannan.jdbc.core;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface RowMapper<T> {
    // map a specific row of result set to an entity
    T mapRom(ResultSet resultSet, int rowNum) throws SQLException;
}
