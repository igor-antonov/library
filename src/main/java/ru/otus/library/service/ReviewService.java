package ru.otus.library.service;

import org.springframework.stereotype.Service;
import ru.otus.library.domain.Book;
import ru.otus.library.domain.Review;
import ru.otus.library.exception.DataNotFoundException;
import ru.otus.library.repository.BookRepository;
import ru.otus.library.repository.ReviewRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final BookRepository bookRepository;

    public ReviewService(ReviewRepository reviewRepository, BookRepository bookRepository) {
        this.reviewRepository = reviewRepository;
        this.bookRepository = bookRepository;
    }

    public String add(String bookId, String reviewer, String text) {
        Optional<Book> bookOpt = bookRepository.findById(bookId);
        return bookOpt.map(book1 -> reviewRepository.insert(new Review(book1, reviewer, text)).getId()).
                orElse("Книга не найдена");
    }

    public String add(String bookId, Review review) {
        Optional<Book> bookOpt = bookRepository.findById(bookId);
        return bookOpt.map(book1 -> reviewRepository.insert(review).getId()).orElse("Книга не найдена");
    }

    public void updateById(String id, Review review) throws DataNotFoundException {
        Optional<Review> reviewOpt = reviewRepository.findById(id);
        if (!reviewOpt.isPresent()){
            throw new DataNotFoundException(String.format("Комментарий по идентификатору %s не найден", id));
        }
        else {
            review.setId(reviewOpt.get().getId());
            reviewRepository.save(review);
        }
    }

    public void updateById(String id, String bookId, String reviewer, String text) throws DataNotFoundException {
        Optional<Review> reviewOpt = reviewRepository.findById(id);
        Optional<Book> bookOpt = bookRepository.findById(bookId);
        if (!reviewOpt.isPresent()){
            throw new DataNotFoundException(String.format("Комментарий по идентификатору %s не найден", id));
        }
        else if(!bookOpt.isPresent()){
            throw new DataNotFoundException(String.format("Книга по идентификатору %s не найдена", bookId));
        }
        else {
            Review review = reviewOpt.get();
            review.setBook(bookOpt.get());
            review.setReviewer(reviewer);
            review.setText(text);
            reviewRepository.save(review);
        }
    }

    public List<String> getByBookId(String bookId) throws DataNotFoundException {
        Optional<Book> bookOpt = bookRepository.findById(bookId);
        if (bookOpt.isPresent()){
            List<String> result = reviewRepository.findByBook(bookOpt.get())
                    .stream().
                            map(Review::toString)
                    .collect(Collectors.toList());
            if (result.isEmpty()){
                throw new DataNotFoundException("Результаты не найдены");
            }
            else {
                return result;
            }
        }
        else {
            throw new DataNotFoundException("Результаты не найдены");
        }
    }

    public String getById(String id) {
        Optional<Review> review = reviewRepository.findById(id);
        if (review.isPresent()){
            return review.get().toString();
        }
        else {
            return "Результаты не найдены";
        }
    }

    public boolean deleteAll(){
        reviewRepository.deleteAll();
        return true;
    }
}
