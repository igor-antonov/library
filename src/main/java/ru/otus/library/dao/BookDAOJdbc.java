package ru.otus.library.dao;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.otus.library.domain.Author;
import ru.otus.library.domain.Book;
import ru.otus.library.domain.BookMapper;
import ru.otus.library.domain.Genre;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class BookDAOJdbc implements BookDAO {

    private final JdbcOperations jdbc;
    private final NamedParameterJdbcOperations parameterJdbc;

    public BookDAOJdbc(JdbcOperations jdbcOperations, NamedParameterJdbcOperations parameterJdbc){
        this.jdbc = jdbcOperations;
        this.parameterJdbc = parameterJdbc;
    }

    @Override
    public Book getById(int id) {
        try{
            Map<String, Object> params = new HashMap<>();
            params.put("id", id);
            return parameterJdbc.queryForObject("select b.*, a.first_name as 'author_first_name', " +
                            "a.second_name as 'author_second_name',\n" +
                            "a.birthday as 'author_birthday', g.genre_name from BOOKS b, GENRES g, AUTHORS a \n" +
                            "where b.genre_id = g.id\n" +
                            "and b.author_id = a.id and b.id =:id",
                    params, new BookMapper());
        }
        catch (EmptyResultDataAccessException e){
            return null;
        }
    }

    @Override
    public List<Book> getByTitle(String title) {
        Map<String, Object> params = new HashMap<>();
        params.put("title", title);
        return parameterJdbc.query("select b.*, a.first_name as 'author_first_name', " +
                        "a.second_name as 'author_second_name',\n" +
                        "a.birthday as 'author_birthday', g.genre_name from BOOKS b, GENRES g, AUTHORS a \n" +
                        "where b.genre_id = g.id\n" +
                        "and b.author_id = a.id and title =:title",
                params, new BookMapper());
    }

    @Override
    public List<Book> getByAuthor(Author author) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", author.getId());
        params.put("first_name", author.getFirstName());
        params.put("birthday", author.getBirthday());
        params.put("second_name", author.getSecondName());
        return parameterJdbc.query("select b.*, a.first_name as 'author_first_name', " +
                        "a.second_name as 'author_second_name',\n" +
                        "a.birthday as 'author_birthday', g.genre_name from BOOKS b, GENRES g, AUTHORS a \n" +
                        "where b.genre_id = g.id\n" +
                        "and b.author_id = a.id and a.id = :id and a.first_name = :first_name" +
                        " and a.second_name = :second_name and a.birthday = :birthday",
                params, new BookMapper());
    }

    @Override
    public List<Book> getByGenre(Genre genre) {
        Map<String, Object> params = new HashMap<>();
        params.put("genre_name", genre.getName());
        return parameterJdbc.query("select b.*, a.first_name as 'author_first_name', " +
                "a.second_name as 'author_second_name',\n" +
                "a.birthday as 'author_birthday', g.genre_name from BOOKS b, GENRES g, AUTHORS a \n" +
                "where b.genre_id = g.id\n" +
                "and b.author_id = a.id and g.genre_name = :genre_name", params, new BookMapper());
    }

    @Override
    public List<Book> getAll() {
        return jdbc.query("select b.*, a.first_name as 'author_first_name', " +
                "a.second_name as 'author_second_name',\n" +
                "a.birthday as 'author_birthday', g.genre_name from BOOKS b, GENRES g, AUTHORS a \n" +
                "where b.genre_id = g.id\n" +
                "and b.author_id = a.id", new BookMapper());
    }

    @Override
    public int insert(Book book) {
        KeyHolder key = new GeneratedKeyHolder();
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("title", book.getTitle());
        params.addValue("authorId", book.getAuthor().getId());
        params.addValue("genreId", book.getGenre().getId());
        parameterJdbc.update("insert into books (title, author_id, genre_id) " +
                "values (:title, :authorId, :genreId)", params, key);
        return key.getKey().intValue();
    }

    @Override
    public void deleteByTitle(String title) {
        MapSqlParameterSource param = new MapSqlParameterSource();
        param.addValue("title",title);
        parameterJdbc.update("delete from books where title=:title", param);
    }

    @Override
    public void deleteAll() {
        jdbc.update("delete from books");
    }

    @Override
    public int updateById(int id, Book book) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", id);
        params.addValue("title", book.getTitle());
        params.addValue("authorId", book.getAuthor().getId());
        params.addValue("genreId", book.getGenre().getId());
        return parameterJdbc.update("update books set title =:title, author_id =:authorId," +
                " genre_id =:genreId where id =:id", params);
    }

    @Override
    public int count() {
        return jdbc.queryForObject("select count(*) from books", Integer.class);
    }
}
