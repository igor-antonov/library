package ru.otus.library.repository;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringRunner;
import ru.otus.library.domain.Author;
import ru.otus.library.domain.Book;
import ru.otus.library.domain.Genre;
import ru.otus.library.exception.DataNotFoundException;

import java.sql.Date;
import java.util.Collections;

@RunWith(SpringRunner.class)
@ComponentScan
@DataJpaTest
public class BookRepositoryTest {

    @Autowired
    GenreRepository genreRepository;
    @Autowired
    AuthorRepository authorRepository;
    @Autowired
    BookRepository bookRepository;
    @Autowired
    TestEntityManager tem;

    Long genreId;
    Genre genre;
    Long authorId;
    Author author;
    Long bookId;
    Book book;

    @Before
    public void prepare(){
        genre = genreRepository.save(new Genre("Comedian"));
        authorId = authorRepository.save(new Author("Иван", "Бунин", Date.valueOf("1870-10-20"))).getId();
        author = tem.find(Author.class, authorId);
        bookId = bookRepository.save(new Book("Сказки", author, genre)).getId();
        book = tem.find(Book.class, bookId);
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
        Assertions.assertThat(bookRepository.findByGenre(genre).get(0).getTitle()).isEqualTo(book.getTitle());
    }

    @Test
    public void findByAuthor(){
        Assertions.assertThat(bookRepository.findByAuthor(author).get(0).getTitle()).isEqualTo(book.getTitle());
    }

    @Test
    public void getAll(){
        Assertions.assertThat(bookRepository.findAll()).isEqualTo(Collections.singletonList(book));
    }

    @Test
    public void deleteByTitle() throws DataNotFoundException {
        long bookId2 = tem.persistAndGetId(new Book("Война и мир", author, genre), Long.class);
        Assertions.assertThat(bookRepository.findById(bookId2).get().getTitle()).isEqualTo("Война и мир");
        bookRepository.deleteByTitle("Война и мир");
        Assertions.assertThat(bookRepository.findById(bookId2).isPresent()).isEqualTo(false);
    }

    @Test
    public void deleteAll(){
        bookRepository.deleteAll();
        Assertions.assertThat(bookRepository.findAll().size()).isEqualTo(0);
        bookId = bookRepository.save(new Book(book.getTitle(), author, genre)).getId();
        book = tem.find(Book.class, bookId);
    }

    @Test
    public void count(){
        Assertions.assertThat(bookRepository.count()).isEqualTo(1);
    }

    @Test
    public void update(){
        book.setTitle("Сто лет одиночества");
        bookRepository.updateById(bookId, book);
        Assertions.assertThat(tem.find(Book.class, bookId).getTitle()).isEqualTo(book.getTitle());
    }
}
