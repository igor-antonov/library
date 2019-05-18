package ru.otus.library.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.library.exception.DataNotFoundException;
import ru.otus.library.repository.BookRepository;
import ru.otus.library.domain.Book;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class BookService {

    private final BookRepository bookRepository;
    private final Logger log = LoggerFactory.getLogger(BookService.class);

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @HystrixCommand(fallbackMethod = "fallbackBook", groupKey = "BookService", commandKey = "add")
    public Book add(Book book) {
        return bookRepository.insert(book);
    }

    @HystrixCommand(fallbackMethod = "fallbackAll", groupKey = "BookService", commandKey = "getAll")
    public List<Book> getAll() {
        return bookRepository.findAll();
    }

    @HystrixCommand(fallbackMethod = "fallbackBook", groupKey = "BookService", commandKey = "getById")
    public Book getById(String id) throws DataNotFoundException {
        Optional<Book> bookOpt = bookRepository.findById(id);
        if (!bookOpt.isPresent()){
            throw new DataNotFoundException(String.format("Книга по идентификатору %s не найдена", id));
        }
        else {
            return bookOpt.get();
        }
    }

    @HystrixCommand(fallbackMethod = "fallbackId", groupKey = "BookService", commandKey = "updateById")
    public void updateById(String bookId, Book book) throws DataNotFoundException {
        Book bookOld = getById(bookId);
        book.setId(bookOld.getId());
        bookRepository.save(book);
    }

    @HystrixCommand(fallbackMethod = "fallbackId", groupKey = "BookService", commandKey = "deleteById")
    public void deleteById(String id) throws DataNotFoundException {
        getById(id);
        bookRepository.deleteById(id);
    }

    public void fallbackId(String bookId, Book book) throws DataNotFoundException {
        String errMessage = "Что-то пошло не так. Обратитесь к администратору";
        log.error(errMessage);
        throw new DataNotFoundException(errMessage);
    }

    public void fallbackId(String id) throws DataNotFoundException {
        String errMessage = "Что-то пошло не так. Обратитесь к администратору";
        log.error(errMessage);
        throw new DataNotFoundException(errMessage);
    }

    public Book fallbackBook(String id) throws DataNotFoundException {
        String errMessage = "Что-то пошло не так. Обратитесь к администратору";
        log.error(errMessage);
        throw new DataNotFoundException(errMessage);
    }

    public Book fallbackBook(Book book) throws DataNotFoundException {
        String errMessage = "Что-то пошло не так. Обратитесь к администратору";
        log.error(errMessage);
        throw new DataNotFoundException(errMessage);
    }

    public List<Book> fallbackAll() throws DataNotFoundException {
        String errMessage = "Что-то пошло не так. Обратитесь к администратору";
        log.error(errMessage);
        throw new DataNotFoundException(errMessage);
    }
}
