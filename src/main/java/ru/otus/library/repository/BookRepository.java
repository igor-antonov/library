package ru.otus.library.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.library.domain.Book;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends MongoRepository<Book, Long> {
    Optional<Book> findById(String id);
    List<Book> findByTitle(String title);
    List<Book> findByAuthor_SecondName(String secondName);
    List<Book> findByGenre_Name(String genreName);
    List<Book> findAll();
    Book insert(Book book);
    long deleteByTitle(String title);
    void deleteAll();
    long count();
}
