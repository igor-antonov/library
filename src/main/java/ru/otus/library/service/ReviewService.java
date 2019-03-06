package ru.otus.library.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.library.domain.Book;
import ru.otus.library.domain.Review;
import ru.otus.library.exception.DataNotFoundException;
import ru.otus.library.repository.BookRepository;
import ru.otus.library.repository.ReviewRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final BookRepository bookRepository;

    public ReviewService(ReviewRepository reviewRepository, BookRepository bookRepository) {
        this.reviewRepository = reviewRepository;
        this.bookRepository = bookRepository;
    }

    public long add(long bookId, String reviewer, String text) {
        Optional<Book> bookOpt = bookRepository.findById(bookId);
        return bookOpt.map(book1 -> reviewRepository.save(new Review(book1, reviewer, text)).getId()).orElse(-1L);
    }

    public long add(long bookId, Review review) {
        Optional<Book> bookOpt = bookRepository.findById(bookId);
        return bookOpt.map(book1 -> reviewRepository.save(review).getId()).orElse(-1L);
    }

    public boolean updateById(long id, Review review) {
        Optional<Book> bookOpt = bookRepository.findById(review.getBook().getId());
        return bookOpt.filter(book -> reviewRepository.updateById(id, review) > 0).isPresent();
    }

    public boolean updateById(long id, long bookId, String reviewer, String text) {
        Optional<Book> bookOpt = bookRepository.findById(bookId);
        return bookOpt.filter(book -> reviewRepository.updateById(id, new Review(book, reviewer, text)) > 0).isPresent();
    }

    public List<String> getByBookId(long bookId) throws DataNotFoundException {
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

    public String getById(long id) {
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
