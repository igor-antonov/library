package ru.otus.library;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringRunner;
import ru.otus.library.domain.Author;
import ru.otus.library.domain.Book;
import ru.otus.library.domain.Genre;
import ru.otus.library.exception.DataNotFoundException;
import ru.otus.library.repository.AuthorRepository;
import ru.otus.library.repository.BookRepository;
import ru.otus.library.repository.GenreRepository;

import java.time.LocalDate;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@ComponentScan
@DataJpaTest
public class BookRepositoryTest {

    @Autowired
    BookRepository bookRepository;
    @Autowired
    AuthorRepository authorRepository;
    @Autowired
    GenreRepository genreRepository;

    private Long bookId;
    private Book book;
    private Author author;
    private Genre genre;


    @Before
    public void prepare(){
        author = authorRepository.save(new Author("Иван", "Бунин", LocalDate.of(1870, 10, 20)));
        genre = genreRepository.save(new Genre("Comedian"));
        bookId = bookRepository.save(new Book("Сказки", author, genre)).getId();
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
        Long bookId2 = bookRepository.save(new Book("Война и мир", author, genre)).getId();
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