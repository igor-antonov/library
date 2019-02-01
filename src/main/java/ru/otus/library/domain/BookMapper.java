package ru.otus.library.domain;

import org.springframework.jdbc.core.RowMapper;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BookMapper implements RowMapper<Book> {

    @Override
    public Book mapRow(ResultSet resultSet, int i) throws SQLException {
        long id = resultSet.getLong("id");
        String title = resultSet.getString("title");
        int genreId = resultSet.getInt("genre_id");
        String genreName = resultSet.getString("genre_name");
        int authorId = resultSet.getInt("author_id");
        String authorFirstName = resultSet.getString("author_first_name");
        String authorSecondName = resultSet.getString("author_second_name");
        Date authorBirthday = resultSet.getDate("author_birthday");
        return new Book(id, title, new Author(authorId, authorFirstName, authorSecondName, authorBirthday),
                new Genre(genreId, genreName));
    }
}
