package com.nannan.jdbc.core;

import lombok.Data;

import javax.sql.DataSource;
import java.sql.*;
import java.util.List;

@Data
public class JdbcTemplate {
    private DataSource dataSource;

    public Object query(StatementCallback callback){
        Connection connection = null;
        Statement statement = null;

        try{
            connection = dataSource.getConnection();
            statement = connection.createStatement();

            return callback.doInStatement(statement);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                statement.close();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    public Object query(String sql, Object[] args, PrepareStatementCallBack prepareStatementCallBack){
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try{
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(sql);

            ArgumentPreparedStatementSetter argumentPreparedStatementSetter = new ArgumentPreparedStatementSetter(args);
            argumentPreparedStatementSetter.setValues(preparedStatement);
            return prepareStatementCallBack.doInPreparedStatement(preparedStatement);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try{
                preparedStatement.close();
                connection.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return null;
    }

    public <T> List<T> batchQuery(String sql, Object[] args, RowMapper<T> rowMapper){
        RowMapperResultSetExtractor<T> resultSetExtractor = new RowMapperResultSetExtractor<>(rowMapper);
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try{
            connection = dataSource.getConnection();
             preparedStatement = connection.prepareStatement(sql);
            ArgumentPreparedStatementSetter argumentPreparedStatementSetter = new ArgumentPreparedStatementSetter(args);
            argumentPreparedStatementSetter.setValues(preparedStatement);
            ResultSet resultSet = preparedStatement.executeQuery();

            return resultSetExtractor.extractData(resultSet);
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            try {
                preparedStatement.close();
                connection.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return null;
    }
}
