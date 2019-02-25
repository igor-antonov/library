package ru.otus.library.service;

import org.springframework.stereotype.Service;
import ru.otus.library.domain.Book;
import ru.otus.library.domain.Review;
import ru.otus.library.exception.DataNotFoundException;
import ru.otus.library.repository.BookRepository;
import ru.otus.library.repository.ReviewRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final BookRepository bookRepository;
    private Book book;

    public ReviewService(ReviewRepository reviewRepository, BookRepository bookRepository) {
        this.reviewRepository = reviewRepository;
        this.bookRepository = bookRepository;
    }

    public long add(long bookId, String reviewer, String text) {
        try{
            book = bookRepository.getById(bookId);
            book.getId();
            return reviewRepository.insert(new Review(book, reviewer, text));
        }
        catch (NullPointerException | DataNotFoundException e){
            return -1;
        }

    }

    public boolean updateById(long id, long bookId, String reviewer, String text){
        try {
            book = bookRepository.getById(bookId);
            return reviewRepository.updateById(id, new Review(book, reviewer, text));
        } catch (DataNotFoundException e) {
            return false;
        }
    }

    public List<String> getByBookId(long bookId) throws DataNotFoundException {
        Book book = bookRepository.getById(bookId);
        if (book == null){
            throw new DataNotFoundException("results not found");
        }

        List<String> result = reviewRepository.getByBook(book)
                .stream().
                        map(Review::toString)
                .collect(Collectors.toList());
        if (result.isEmpty()){
            throw new DataNotFoundException("results not found");
        }
        else {
            return result;
        }
    }

    public String getById(long id) {
        try {
            return reviewRepository.getById(id).toString();
        }
        catch (NullPointerException e){
            return "results not found";
        }
    }

    public boolean deleteAll(){
        return reviewRepository.deleteAll();
    }
}
