package com.nannan.jdbc.core;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface PrepareStatementCallBack {
    Object doInPreparedStatement(PreparedStatement preparedStatement) throws SQLException;
}
