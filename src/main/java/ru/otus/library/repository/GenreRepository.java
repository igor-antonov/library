package ru.otus.library.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import ru.otus.library.domain.Genre;

import java.util.List;
import java.util.Optional;

public interface GenreRepository extends CrudRepository<Genre, Long> {
    Optional<Genre> findById(long id);
    Optional<Genre> findByName(String name);
    List<Genre> findAll();
    Genre save(Genre genre);
    long deleteByName(String name);
    void deleteAll();

    @Modifying
    @Query(value = "update Genre set genre_name = :newName where genre_name = :oldName")
    int updateByName(@Param("oldName") String oldName, @Param("newName") String newName);
    long count();
}
