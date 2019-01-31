package ru.otus.library.dao;

import ru.otus.library.domain.Author;
import ru.otus.library.domain.Book;
import ru.otus.library.domain.Genre;

import java.util.List;

public interface BookDAO {
    Book getById(int id);
    List<Book> getByTitle(String title);
    List<Book> getByAuthor(Author author);
    List<Book> getByGenre(Genre genre);
    List<Book> getAll();
    int insert(Book book);
    void deleteByTitle(String title);
    void deleteAll();
    void updateById(int id, Book book);
    int count();
}
