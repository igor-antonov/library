package ru.otus.library.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import ru.otus.library.domain.Book;

import java.util.List;
import java.util.Optional;


public interface BookRepository extends CrudRepository<Book, Long> {

    Optional<Book> findById(Long id);
    List<Book> findAll();
    Book save(@Param("book") Book book);
    void deleteById(@Param("id") Long id);
}