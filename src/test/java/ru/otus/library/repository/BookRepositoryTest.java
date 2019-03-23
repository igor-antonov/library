package ru.otus.library.repository;

import org.assertj.core.api.Assertions;
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
        Assertions.assertThat(bookRepository.findById(bookId).get().getTitle()).isEqualTo(book.getTitle());
    }

    @Test
    public void findByTitle(){
        Assertions.assertThat(bookRepository.findByTitle(book.getTitle()).get(0).getTitle()).isEqualTo(book.getTitle());
    }

    @Test
    public void findByGenre(){
        Assertions.assertThat(bookRepository.findByGenre_Name(
                genre.getName()).get(0).getTitle()).isEqualTo(book.getTitle());
    }

    @Test
    public void findByAuthor(){
        Assertions.assertThat(bookRepository.findByAuthor_SecondName(
                author.getSecondName()).get(0).getTitle()).isEqualTo(book.getTitle());
    }

    @Test
    public void getAll(){
        Assertions.assertThat(
                bookRepository.findAll()).isEqualTo(Collections.singletonList(book));
    }

    @Test
    public void deleteByTitle() throws DataNotFoundException {
        String bookId2 = bookRepository.insert(new Book("Война и мир", author, genre)).getId();
        Assertions.assertThat(bookRepository.findById(bookId2).get().getTitle()).isEqualTo("Война и мир");
        bookRepository.deleteByTitle("Война и мир");
        Assertions.assertThat(bookRepository.findById(bookId2).isPresent()).isEqualTo(false);
    }

    @Test
    public void deleteAll(){
        bookRepository.deleteAll();
        Assertions.assertThat(bookRepository.findAll().size()).isEqualTo(0);
        bookId = bookRepository.save(new Book(book.getTitle(), author, genre)).getId();
        book = bookRepository.findById(bookId).get();
    }

    @Test
    public void count(){
        Assertions.assertThat(bookRepository.count()).isGreaterThanOrEqualTo(1);
    }

    @Test
    public void update(){
        book.setTitle("Сто лет одиночества");
        bookRepository.save(book);
        Assertions.assertThat(bookRepository.findById(bookId).get().getTitle()).isEqualTo(book.getTitle());
    }
}
