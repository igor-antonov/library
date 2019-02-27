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
import java.util.Optional;

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
        author.setId(1L);
        genre = new Genre("Повесть");
        genre.setId(2L);
        book = new Book("Вий", author, genre);
        book.setId(5L);
    }

    @Test
    public void addNewBook() throws DataNotFoundException {
        given(authorRepository.findById(author.getId())).willReturn(Optional.ofNullable(author));
        given(genreRepository.findById(genre.getId())).willReturn(Optional.ofNullable(genre));
        given(bookRepository.save(new Book("1984", author, genre))).willReturn(book);
        Assertions.assertThat(bookService.add("1984", author.getId(), genre.getId()))
                .isGreaterThanOrEqualTo(0L);
    }

    @Test
    public void testFailureUpdate() {
        given(authorRepository.findById(2)).willReturn(Optional.of(author));
        try {
            bookService.updateById(7, "Вий", 2, 4);
        }
        catch (DataNotFoundException ex){
            Assertions.assertThat("Жанр по идентификатору 4 не найден").isEqualTo(ex.getMessage());
        }
    }

    @Test
    public void getBookByTitle() throws DataNotFoundException {
        given(bookRepository.findByTitle("Вий")).willReturn(Collections.singletonList(book));
        Assertions.assertThat(bookService.getByTitle("Вий"))
                .isEqualTo(Collections.singletonList(book.toString()));
    }

    @Test
    public void deleteBook() {
        given(bookRepository.deleteByTitle("Вий")).willReturn(book.getId());
        Assertions.assertThat(bookService.deleteByTitle("Вий"))
                .isEqualTo(true);
    }

    @Test
    public void deleteAll() {
        Assertions.assertThat(bookService.deleteAll()).isEqualTo(true);
    }
}