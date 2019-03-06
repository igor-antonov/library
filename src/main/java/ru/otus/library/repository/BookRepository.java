package ru.otus.library.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.library.domain.Author;
import ru.otus.library.domain.Book;
import ru.otus.library.domain.Genre;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends CrudRepository<Book, Long> {
    Optional<Book> findById(long id);
    List<Book> findByTitle(String title);
    List<Book> findByAuthor(Author author);
    List<Book> findByGenre(Genre genre);
    List<Book> findAll();
    Book save(Book book);
    long deleteByTitle(String title);
    @Transactional
    void deleteAll();

    @Modifying
    @Query(value = "update Book b set b =:book where id =:id")
    int updateById(@Param("id") long id, @Param("book") Book book);
    long count();
}
