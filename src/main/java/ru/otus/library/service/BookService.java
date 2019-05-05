package ru.otus.library.service;

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

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public Book add(Book book) {
        return bookRepository.save(book);
    }

    public List<Book> getAll() {
        return bookRepository.findAll();
    }

    public Book getById(Long id) throws DataNotFoundException {
        Optional<Book> bookOpt = bookRepository.findById(id);
        if (!bookOpt.isPresent()){
            throw new DataNotFoundException(String.format("Книга по идентификатору %s не найдена", id));
        }
        else {
            return bookOpt.get();
        }
    }

    public Book update(Long bookId, Book book) throws DataNotFoundException {
        getById(bookId);
        book.setId(bookId);
        System.out.println("authorrrrR: " + book.getAuthor());
        return bookRepository.save(book);
    }

    public void deleteById(Long id) throws DataNotFoundException {
        getById(id);
        bookRepository.deleteById(id);
    }
}
