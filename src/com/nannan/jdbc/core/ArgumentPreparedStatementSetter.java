package com.nannan.jdbc.core;

import java.sql.PreparedStatement;
import java.sql.SQLException;

//批量设置sql参数
public class ArgumentPreparedStatementSetter {
    private final Object[] sqlArgs; //sql参数

    public ArgumentPreparedStatementSetter(Object[] sqlArgs) {
        this.sqlArgs = sqlArgs;
    }

    public void setValues(PreparedStatement preparedStatement) throws SQLException {
        if (this.sqlArgs != null) {
            for (int i = 0; i < this.sqlArgs.length; i++) {
                doSetValue(preparedStatement, i, sqlArgs[i]);
            }
        }
    }

    protected void doSetValue(PreparedStatement preparedStatement, int paramPosition, Object argValue) throws SQLException {
        Object arg = argValue;
        if (arg instanceof String) {
            preparedStatement.setString(paramPosition, (String) arg);
        } else if (arg instanceof Integer) {
            preparedStatement.setInt(paramPosition, (int) arg);
        } else if (arg instanceof java.util.Date) {
            preparedStatement.setDate(paramPosition, new java.sql.Date(((java.util.Date) arg).getTime()));
        }
        //todo 传参类型待完善
    }
}
