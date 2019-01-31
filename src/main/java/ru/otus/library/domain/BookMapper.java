package ru.otus.library.domain;

import org.springframework.jdbc.core.RowMapper;
import ru.otus.library.service.BookService;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BookMapper implements RowMapper<Book> {

    BookService bookService;

    public BookMapper(BookService bookService){
        this.bookService = bookService;
    }

    @Override
    public Book mapRow(ResultSet resultSet, int i) throws SQLException {
        long id = resultSet.getLong("id");
        String title = resultSet.getString("title");
        int genreId = resultSet.getInt("genre_id");
        int authorId = resultSet.getInt("author_id");
        Author author = bookService.getAuthor(authorId);
        Genre genre = bookService.getGenre(genreId);
        return new Book(id, title, author, genre);
    }
}
