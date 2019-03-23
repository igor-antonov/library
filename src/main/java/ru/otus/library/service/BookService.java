package ru.otus.library.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.library.exception.DataNotFoundException;
import ru.otus.library.repository.BookRepository;
import ru.otus.library.domain.Book;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    public List<String> getByTitle(String title) throws DataNotFoundException {
        List<String> result = bookRepository.findByTitle(title)
                .stream().
                        map(Book::toString)
                .collect(Collectors.toList());
        if (result.isEmpty()){
            throw new DataNotFoundException("Результаты не найдены");
        }
        else {
            return result;
        }
    }



    public void updateById(String bookId, Book book) throws DataNotFoundException {
        Optional<Book> bookOpt = bookRepository.findById(bookId);
        if (!bookOpt.isPresent()){
            throw new DataNotFoundException(String.format("Книга по идентификатору %s не найдена", bookId));
        }
        else {
            book.setId(bookOpt.get().getId());
            bookRepository.save(book);
        }
    }

    public boolean deleteByTitle(String title){
        return bookRepository.deleteByTitle(title) > 0;
    }

    public boolean deleteAll(){
        bookRepository.deleteAll();
        return true;
    }


    public List<String> getByAuthorSecondName(String secondName) throws DataNotFoundException {
        List<String> result = bookRepository.findByAuthor_SecondName(secondName)
                .stream().
                        map(Book::toString)
                .collect(Collectors.toList());
        if (result.isEmpty()){
            throw new DataNotFoundException("Результаты не найдены");
        }
        else {
            return result;
        }
    }

    public List<String> getByGenreName(String genreName) throws DataNotFoundException {
        List<String> result = bookRepository.findByGenre_Name(genreName)
                .stream().
                        map(Book::toString)
                .collect(Collectors.toList());
        if (result.isEmpty()){
            throw new DataNotFoundException("Результаты не найдены");
        }
        else {
            return result;
        }
    }
}
