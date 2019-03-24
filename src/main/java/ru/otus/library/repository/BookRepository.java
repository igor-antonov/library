package ru.otus.library.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.library.domain.Book;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends MongoRepository<Book, Long> {
    Optional<Book> findById(String id);
    List<Book> findAll();
    Book insert(Book book);
    void deleteById(String id);
}
