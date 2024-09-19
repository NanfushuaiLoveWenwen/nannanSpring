package com.nannan.jdbc.core;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface ResultSetExtractor<T> {
    //convert result set to an entity
    T extractData(ResultSet resultSet) throws SQLException;
}
