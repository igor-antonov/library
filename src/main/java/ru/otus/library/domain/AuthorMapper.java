package ru.otus.library.domain;

import org.springframework.jdbc.core.RowMapper;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AuthorMapper implements RowMapper<Author> {
    @Override
    public Author mapRow(ResultSet resultSet, int i) throws SQLException {
        long id = resultSet.getLong("id");
        String firstName = resultSet.getString("first_name");
        String secondName = resultSet.getString("second_name");
        Date birthday = resultSet.getDate("birthday");
        return new Author(id, firstName, secondName, birthday);
    }
}