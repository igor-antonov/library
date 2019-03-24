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
        return bookRepository.insert(book);
    }

    public List<Book> getAll() {
        return bookRepository.findAll();
    }

    public Book getById(String id) throws DataNotFoundException {
        Optional<Book> bookOpt = bookRepository.findById(id);
        if (!bookOpt.isPresent()){
            throw new DataNotFoundException(String.format("Книга по идентификатору %s не найдена", id));
        }
        else {
            return bookOpt.get();
        }
    }

    public void updateById(String bookId, Book book) throws DataNotFoundException {
        Book bookOld = getById(bookId);
        book.setId(bookOld.getId());
        bookRepository.save(book);
    }

    public void deleteById(String id) throws DataNotFoundException {
        getById(id);
        bookRepository.deleteById(id);
    }
}
