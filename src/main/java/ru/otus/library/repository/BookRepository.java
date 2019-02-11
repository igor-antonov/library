package ru.otus.library.repository;

import ru.otus.library.domain.Author;
import ru.otus.library.domain.Book;
import ru.otus.library.domain.Genre;
import java.util.List;

public interface BookRepository {
    Book getById(long id);
    List<Book> getByTitle(String title);
    List<Book> getByAuthor(Author author);
    List<Book> getByGenre(Genre genre);
    List<Book> getAll();
    long insert(Book book);
    void deleteByTitle(String title);
    void deleteAll();
    boolean updateById(long id, Book book);
    long count();
}
