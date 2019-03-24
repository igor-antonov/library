package ru.otus.library;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringRunner;
import ru.otus.library.domain.Author;
import ru.otus.library.domain.Book;
import ru.otus.library.domain.Genre;
import ru.otus.library.exception.DataNotFoundException;
import ru.otus.library.repository.BookRepository;

import java.time.LocalDate;
import java.util.Collections;

@RunWith(SpringRunner.class)
@ComponentScan
@DataMongoTest
public class BookRepositoryTest {


    @Autowired
    BookRepository bookRepository;

    private String bookId;
    private Book book;
    private Author author;
    private Genre genre;


    @Before
    public void prepare(){
        author = new Author("Иван", "Бунин", LocalDate.of(1870, 10, 20));
        genre = new Genre("Comedian");
        bookId = bookRepository.insert(new Book("Сказки", author, genre)).getId();
        book = bookRepository.findById(bookId).get();
    }

    @Test
    public void findById() {
        assertThat(bookRepository.findById(bookId).get().getTitle()).isEqualTo(book.getTitle());
    }

    @Test
    public void getAll(){
        assertThat(
                bookRepository.findAll()).isEqualTo(Collections.singletonList(book));
    }

    @Test
    public void deleteById() throws DataNotFoundException {
        String bookId2 = bookRepository.insert(new Book("Война и мир", author, genre)).getId();
        assertThat(bookRepository.findById(bookId2).get().getTitle()).isEqualTo("Война и мир");
        bookRepository.deleteById(bookId2);
        assertThat(bookRepository.findById(bookId2).isPresent()).isEqualTo(false);
    }

    @Test
    public void update(){
        book.setTitle("Сто лет одиночества");
        bookRepository.save(book);
        assertThat(bookRepository.findById(bookId).get().getTitle()).isEqualTo(book.getTitle());
    }
}
