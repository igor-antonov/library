package ru.otus.library.service;

import org.springframework.stereotype.Service;
import ru.otus.library.domain.Book;
import ru.otus.library.domain.Review;
import ru.otus.library.repository.BookRepository;
import ru.otus.library.repository.ReviewRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final BookRepository bookRepository;

    public ReviewService(ReviewRepository reviewRepository, BookRepository bookRepository) {
        this.reviewRepository = reviewRepository;
        this.bookRepository = bookRepository;
    }

    public long add(long bookId, String reviewer, String text) {
        Book book = bookRepository.getById(bookId);
        if (book == null){
            return -1;
        }
        return reviewRepository.insert(new Review(book, reviewer, text));
    }

    public boolean updateById(long id, long bookId, String reviewer, String text){
        Book book = bookRepository.getById(bookId);
        if (book == null){
            return false;
        }
        else {
            return reviewRepository.updateById(id, new Review(book, reviewer, text));
        }
    }

    public List<String> getByBookId(long bookId){
        ArrayList<String> reviews = new ArrayList<>();
        Book book = bookRepository.getById(bookId);
        if (book == null){
            reviews.add(String.format("Книга по идентификатору %d не найдена", bookId));
            return reviews;
        }
        for (Review review: reviewRepository.getByBook(book)){
            reviews.add(review.toString());
        }
        if (reviews.size()>0){
            return reviews;
        }
        else {
            return null;
        }
    }

    public boolean deleteAll(){
        reviewRepository.deleteAll();
        return true;
    }
}
