package ru.otus.library.repository;

import ru.otus.library.domain.Book;
import ru.otus.library.domain.Review;
import java.util.List;

public interface ReviewRepository {
    Review getById(long id);
    List<Review> getByBook(Book book);
    long insert(Review review);
    boolean deleteAll();
    boolean updateById(long id, Review review);
}
