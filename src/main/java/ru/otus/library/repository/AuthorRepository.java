package ru.otus.library.repository;

import ru.otus.library.domain.Author;
import ru.otus.library.exception.DataNotFoundException;

import java.sql.Date;
import java.util.List;

public interface AuthorRepository {
    Author getById(long id);
    List<Author> getByFirstNameAndSecondName(String firstName, String secondName);
    List<Author> getBySecondName(String secondName);
    List<Author> getByBirthday(Date birthday);
    List<Author> getAll();
    long insert(Author author);
    boolean deleteById(long id);
    boolean deleteAll();
    boolean updateBySecondName(String oldSecondName, String firstName, String secondName, Date birthday) throws DataNotFoundException;
    boolean updateById(int id, String firstName, String secondName, Date birthday);
    long count();
}
