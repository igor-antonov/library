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
    @Autowired
    BookService bookService;
    private Book book;

    @Before
    public void prepare() {
        book = new Book("Вий"
                , new Author("Иван", "Бунин", Date.valueOf("1870-10-20"))
                , new Genre("Повесть"));
        book.setId(5L);
    }

    @Test
    public void addNewBookAuthorNotFound() {
        given(bookRepository.insert(book)).willReturn(-1L);
        Assertions.assertThat(bookService.add("1984", 1, 2))
                .isEqualTo(-1L);
    }

    @Test
    public void testUpdateAuthorNotFound() {
        given(bookRepository.updateById(7L, book)).willReturn(false);
        Assertions.assertThat(bookService.updateById(7, "Вий", 2, 4))
                .isEqualTo(-1);
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