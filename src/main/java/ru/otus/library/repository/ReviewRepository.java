package ru.otus.library.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import ru.otus.library.domain.Book;
import ru.otus.library.domain.Review;
import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends CrudRepository<Review, Long> {

    Optional<Review> findById(long id);
    List<Review> findByBook(Book book);
    List<Review> findAll();
    Review save(Review review);
    void deleteAll();

    @Modifying
    @Query(value = "update Review r set r =:review where id =:id")
    int updateById(@Param("id") long id, @Param("review") Review review);

    long count();
}
