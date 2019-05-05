package ru.otus.library.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import ru.otus.library.domain.Author;
import ru.otus.library.domain.Book;
import ru.otus.library.domain.Genre;
import ru.otus.library.exception.DataNotFoundException;
import ru.otus.library.repository.BookRepository;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ServiceBookTest {
    @MockBean
    BookRepository bookRepository;
    @Autowired
    BookService bookService;
    private Book book;
    private Author author;
    private Genre genre;

    @Before
    public void prepare() {
        author = new Author("Иван", "Бунин", LocalDate.of(1870,10,20));
        genre = new Genre("Повесть");
        book = new Book("Вий", author, genre);
    }

    @Test
    public void addNewBook() {
        Book book1 = new Book("1984", author, genre);
        given(bookRepository.save(book1)).willReturn(book1);
        assertThat(bookService.add(book1))
                .isEqualTo(book1);
    }

    @Test
    public void getBookById() throws DataNotFoundException {
        given(bookRepository.findById(123L)).willReturn(Optional.ofNullable(book));
        assertThat(bookService.getById(123L))
                .isEqualTo(book);
    }
}