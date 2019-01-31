package ru.otus.library.dao;

import ru.otus.library.domain.Author;

import java.sql.Date;
import java.util.List;

public interface AuthorDAO {
    Author getById(int id);
    List<Author> getByFirstNameAndSecondName(String firstName, String secondName);
    List<Author> getBySecondName(String secondName);
    List<Author> getByBirthday(Date birthday);
    List<Author> getAll();
    int insert(String firstName, String secondName, Date birthday);
    void deleteBySecondName(String secondName);
    void deleteAll();
    void updateBySecondName(String oldSecondName, String firstName, String secondName, Date birthday);
    int count();
}
