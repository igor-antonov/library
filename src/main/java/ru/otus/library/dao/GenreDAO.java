package ru.otus.library.dao;

import ru.otus.library.domain.Genre;

import java.util.List;

public interface GenreDAO {
    Genre getById(int id);
    Genre getByName(String name);
    List<Genre> getAll();
    int insert(Genre genre);
    void deleteByName(String name);
    void deleteAll();
    void updateByName(String oldName, String newName);
    int count();
}
