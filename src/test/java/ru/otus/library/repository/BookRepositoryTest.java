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
        genreId = genreRepository.insert(new Genre("Comedian"));
        genre = tem.find(Genre.class, genreId);
        authorId = authorRepository.insert(new Author("Иван", "Бунин", Date.valueOf("1870-10-20")));
        author = tem.find(Author.class, authorId);
        bookId = bookRepository.insert(new Book("Сказки", author, genre));
        book = tem.find(Book.class, bookId);
    }

    @Test
    public void findById() throws DataNotFoundException {
        Assertions.assertThat(bookRepository.getById(bookId).getTitle()).isEqualTo(book.getTitle());
    }

    @Test
    public void findByTitle(){
        Assertions.assertThat(bookRepository.getByTitle(book.getTitle()).get(0).getTitle()).isEqualTo(book.getTitle());
    }

    @Test
    public void findByGenre(){
        Assertions.assertThat(bookRepository.getByGenre(genre).get(0).getTitle()).isEqualTo(book.getTitle());
    }

    @Test
    public void findByAuthor(){
        Assertions.assertThat(bookRepository.getByAuthor(author).get(0).getTitle()).isEqualTo(book.getTitle());
    }

    @Test
    public void getAll(){
        Assertions.assertThat(bookRepository.getAll()).isEqualTo(Collections.singletonList(book));
    }

    @Test
    public void deleteByTitle() throws DataNotFoundException {
        long bookId2 = tem.persistAndGetId(new Book("Война и мир", author, genre), Long.class);
        Assertions.assertThat(bookRepository.getById(bookId2).getTitle()).isEqualTo("Война и мир");
        bookRepository.deleteByTitle("Война и мир");
        Assertions.assertThat(bookRepository.getById(bookId2)).isEqualTo(null);
    }

    @Test
    public void deleteAll(){
        bookRepository.deleteAll();
        Assertions.assertThat(bookRepository.getAll().size()).isEqualTo(0);
        bookId = bookRepository.insert(new Book(book.getTitle(), author, genre));
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
