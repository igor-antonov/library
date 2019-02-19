package ru.otus.library.shell;

import org.assertj.core.api.Assertions;
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
import java.sql.Date;
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

    @Test
    public void addNewBook(){
        given(bookService.add("1984", 1, 2)).willReturn(7L);
        Assertions.assertThat(sendShellCommand(String.format("newbook 1984 %s %s", 1, 2)))
                .isEqualTo("Добавлена книга 1984 с идентификатором 7");
    }

    @Test
    public void testUpdate() {
        given(bookService.updateById(7, "Вий", 2, 4)).willReturn(1L);
        Assertions.assertThat(sendShellCommand(
                String.format("editbook %s %s %s %s", 7, "Вий", 2, 4)))
                .isEqualTo("Книга с идентификатором 7 изменена");
    }

    @Test
    public void getBookByTitle() throws DataNotFoundException {
        Book book = new Book("Вий"
                , new Author("Иван", "Бунин", Date.valueOf("1870-10-20"))
                , new Genre("Повесть"));
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