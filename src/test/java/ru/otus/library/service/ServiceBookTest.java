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
import ru.otus.library.repository.BookRepository;

import java.time.LocalDate;
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
        given(bookRepository.insert(book1)).willReturn(book1);
        Assertions.assertThat(bookService.add(book1))
                .isEqualTo(book1);
    }

    @Test
    public void getBookByTitle() throws DataNotFoundException {
        given(bookRepository.findByTitle("Вий")).willReturn(Collections.singletonList(book));
        Assertions.assertThat(bookService.getByTitle("Вий"))
                .isEqualTo(Collections.singletonList(book.toString()));
    }

    @Test
    public void deleteBook() {
        given(bookRepository.deleteByTitle("Вий")).willReturn(1L);
        Assertions.assertThat(bookService.deleteByTitle("Вий"))
                .isEqualTo(true);
    }

    @Test
    public void deleteAll() {
        Assertions.assertThat(bookService.deleteAll()).isEqualTo(true);
    }
}