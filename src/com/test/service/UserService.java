package com.test.service;

import com.nannan.beans.factory.annotation.Autowired;
import com.nannan.jdbc.core.JdbcTemplate;
import com.test.entity.User;

import java.sql.Date;
import java.sql.ResultSet;
import java.util.List;

public class UserService {
    @Autowired
    JdbcTemplate jdbcTemplate;

    //single query
    public User getUserInfo(int id) {
        final String sql = "select id, name, birthday from users where id = ?";

        return (User) jdbcTemplate.query(sql, new Object[]{id}, preparedStatement -> {
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                User user = new User();
                user.setId(id);
                user.setName(resultSet.getString("name"));
                user.setBirthday(resultSet.getDate("birthday"));
                return user;
            }
            return null;
        });
    }

    //batch query
    public List<User> getUsers(int id) {
        final String sql = "select id, name,birthday from users where id>?";
        return jdbcTemplate.batchQuery(sql, new Object[]{id}, (resultSet, rowNum) ->
                new User(id, resultSet.getString("name"), resultSet.getDate("birthday")));
    }
}
