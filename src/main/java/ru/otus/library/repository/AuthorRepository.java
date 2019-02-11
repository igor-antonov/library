package ru.otus.library.repository;

import ru.otus.library.domain.Author;

import java.sql.Date;
import java.util.List;

public interface AuthorRepository {
    Author getById(long id);
    List<Author> getByFirstNameAndSecondName(String firstName, String secondName);
    List<Author> getBySecondName(String secondName);
    List<Author> getByBirthday(Date birthday);
    List<Author> getAll();
    long insert(Author author);
    void deleteById(long id);
    void deleteAll();
    boolean updateBySecondName(String oldSecondName, String firstName, String secondName, Date birthday);
    boolean updateById(int id, String firstName, String secondName, Date birthday);
    long count();
}
