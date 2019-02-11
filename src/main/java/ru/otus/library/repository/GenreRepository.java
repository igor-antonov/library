package ru.otus.library.repository;

import ru.otus.library.domain.Genre;

import java.util.List;

public interface GenreRepository {
    Genre getById(long id);
    Genre getByName(String name);
    List<Genre> getAll();
    long insert(Genre genre);
    void deleteByName(String name);
    void deleteAll();
    void updateByName(String oldName, String newName);
    long count();
}
