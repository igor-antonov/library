package ru.otus.library.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.library.domain.Book;
import ru.otus.library.domain.Review;
import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends MongoRepository<Review, Long> {

    Optional<Review> findById(String id);
    List<Review> findByBook(Book book);
    List<Review> findAll();
    Review save(Review review);
    void deleteAll();
    long count();
}
