package ru.otus.library.shell;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.shell.Shell;
import org.springframework.shell.jline.InteractiveShellApplicationRunner;
import org.springframework.shell.jline.ScriptShellApplicationRunner;
import org.springframework.test.context.junit4.SpringRunner;
import ru.otus.library.domain.Author;
import ru.otus.library.domain.Book;
import ru.otus.library.domain.Genre;
import ru.otus.library.exception.DataNotFoundException;
import ru.otus.library.service.BookService;

import static org.mockito.BDDMockito.given;
import java.time.LocalDate;
import java.util.Collections;

@RunWith(SpringRunner.class)
@SpringBootTest(properties={
        InteractiveShellApplicationRunner.SPRING_SHELL_INTERACTIVE_ENABLED + "=false",
        ScriptShellApplicationRunner.SPRING_SHELL_SCRIPT_ENABLED + "=false"
})
public class ShellCommandBookTest {

    @Autowired
    Shell shell;
    @MockBean
    BookService bookService;
    private Book book;

    @Before
    public void prepare(){
        book = new Book("Вий"
                , new Author("Иван", "Бунин", LocalDate.of(1870,10,20))
                , new Genre("Повесть"));
        book.setId("5c8cc0c8c59f940d10471c42");
    }

    @Test
    public void addNewBook() {
        given(bookService.add(book)).willReturn(book);
        Assertions.assertThat(sendShellCommand(
                String.format("newbook %s %s %s %d %d %d %s",
                        book.getTitle(), book.getAuthor().getFirstName()
                        , book.getAuthor().getSecondName()
                        , book.getAuthor().getBirthday().getYear()
                        , book.getAuthor().getBirthday().getMonthValue()
                        , book.getAuthor().getBirthday().getDayOfMonth()
                        , book.getGenre().getName())))
                .isEqualTo("Добавлена книга с идентификатором: null");
    }

    @Test
    public void testUpdate(){
        Assertions.assertThat(sendShellCommand(
                String.format("editbook %s %s %s %s %d %d %d %s",
                        book.getId(), book.getTitle(), book.getAuthor().getFirstName()
                        , book.getAuthor().getSecondName()
                        , book.getAuthor().getBirthday().getYear()
                        , book.getAuthor().getBirthday().getMonthValue()
                        , book.getAuthor().getBirthday().getDayOfMonth()
                        , book.getGenre().getName())))
                .isEqualTo("Книга с идентификатором 5c8cc0c8c59f940d10471c42 изменена");
    }

    @Test
    public void getBookByTitle() throws DataNotFoundException {

        given(bookService.getByTitle("Вий")).willReturn(Collections.singletonList(book.toString()));
        Assertions.assertThat(sendShellCommand("getbooksbyt Вий"))
                .isEqualTo(Collections.singletonList(book.toString()));
    }

    @Test
    public void deleteBook() {
        given(bookService.deleteByTitle("Вий")).willReturn(true);
        Assertions.assertThat(sendShellCommand("deletebook Вий"))
                .isEqualTo("Книга Вий удалена");
    }

    @Test
    public void deleteAll() {
        given(bookService.deleteAll()).willReturn(true);
        Assertions.assertThat(sendShellCommand("deletebookall")).isEqualTo("Таблица книг очищена");
    }

    public Object sendShellCommand(String command){
        return shell.evaluate(() -> command);
    }
}