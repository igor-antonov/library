package ru.otus.library.service;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.shell.jline.InteractiveShellApplicationRunner;
import org.springframework.shell.jline.ScriptShellApplicationRunner;
import org.springframework.test.context.junit4.SpringRunner;
import ru.otus.library.domain.Author;
import ru.otus.library.domain.Book;
import ru.otus.library.domain.Genre;
import ru.otus.library.exception.DataNotFoundException;
import ru.otus.library.repository.AuthorRepository;
import ru.otus.library.repository.BookRepository;
import ru.otus.library.repository.GenreRepository;

import java.sql.Date;
import java.util.Collections;

import static org.mockito.BDDMockito.given;

@RunWith(SpringRunner.class)
@SpringBootTest(properties={
        InteractiveShellApplicationRunner.SPRING_SHELL_INTERACTIVE_ENABLED + "=false",
        ScriptShellApplicationRunner.SPRING_SHELL_SCRIPT_ENABLED + "=false"
})
public class ServiceBookTest {
    @MockBean
    BookRepository bookRepository;
    @MockBean
    AuthorRepository authorRepository;
    @MockBean
    GenreRepository genreRepository;
    @Autowired
    BookService bookService;
    private Book book;
    private Author author;
    private Genre genre;

    @Before
    public void prepare() {
        author = new Author("Иван", "Бунин", Date.valueOf("1870-10-20"));
        genre = new Genre("Повесть");
        book = new Book("Вий", author, genre);
        book.setId(5L);
    }

    @Test
    public void addNewBook() throws DataNotFoundException {
        given(authorRepository.getById(1)).willReturn(author);
        given(genreRepository.getById(2)).willReturn(genre);
        given(bookRepository.insert(book)).willReturn(0L);
        Assertions.assertThat(bookService.add("1984", 1, 2))
                .isEqualTo(0L);
    }

    @Test
    public void testFailureUpdate() {
        given(authorRepository.getById(2)).willReturn(author);
        try {
            bookService.updateById(7, "Вий", 2, 4);
        }
        catch (DataNotFoundException ex){
            Assertions.assertThat("Жанр по идентификатору 4 не найден").isEqualTo(ex.getMessage());
        }
    }

    @Test
    public void getBookByTitle() throws DataNotFoundException {
        given(bookRepository.getByTitle("Вий")).willReturn(Collections.singletonList(book));
        Assertions.assertThat(bookService.getByTitle("Вий"))
                .isEqualTo(Collections.singletonList(book.toString()));
    }

    @Test
    public void deleteBook() {
        given(bookRepository.deleteByTitle("Вий")).willReturn(true);
        Assertions.assertThat(bookService.deleteByTitle("Вий"))
                .isEqualTo(true);
    }

    @Test
    public void deleteAll() {
        given(bookRepository.deleteAll()).willReturn(true);
        Assertions.assertThat(bookService.deleteAll()).isEqualTo(true);
    }
}