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
import ru.otus.library.exception.DataNotFoundException;
import ru.otus.library.service.AuthorService;

import java.sql.Date;
import java.util.Collections;

import static org.mockito.BDDMockito.given;

@RunWith(SpringRunner.class)
@SpringBootTest(properties={
        InteractiveShellApplicationRunner.SPRING_SHELL_INTERACTIVE_ENABLED + "=false",
        ScriptShellApplicationRunner.SPRING_SHELL_SCRIPT_ENABLED + "=false"
})
public class ShellCommandAuthorTest {

    @Autowired
    Shell shell;
    @MockBean
    AuthorService authorService;

    @Test
    public void testCount() {
        given(authorService.getCount())
                .willReturn(1L);
        Assertions.assertThat(sendShellCommand("getauthorsc")).isEqualTo("Количество авторов: 1");
    }

    @Test
    public void addNewAuthor() {
        given(authorService.add("Иван", "Бунин", Date.valueOf("1870-10-20")))
                .willReturn(1L);
        Assertions.assertThat(sendShellCommand(String.format("newauthor %s %s %s",
                "Иван", "Бунин", Date.valueOf("1870-10-20"))))
                .isEqualTo("Добавлен автор Иван Бунин с идентификатором 1");
    }

    @Test
    public void updateAuthor() throws DataNotFoundException {
        given(authorService.updateBySecondName("Бунин",
                "Николай", "Гоголь", Date.valueOf("1809-03-20")))
                .willReturn(true);
        Assertions.assertThat(sendShellCommand("editauthor Бунин Николай Гоголь 1809-03-20"))
        .isEqualTo("Автор с фамилией Бунин изменен");
    }

    @Test
    public void findBySecondName() throws DataNotFoundException {
        Author author = new Author("Иван", "Бунин", Date.valueOf("1870-10-20"));
        given(authorService.getBySecondName("Бунин"))
                .willReturn(Collections.singletonList(author.toString()));
        Assertions.assertThat(sendShellCommand("getauthorss Бунин"))
                .isEqualTo(Collections.singletonList(author.toString()));
    }

    @Test
    public void deleteAuthorById(){
        given(authorService.deleteById(1)).willReturn(true);
        Assertions.assertThat(sendShellCommand("deleteauthor 1"))
                .isEqualTo("Автор с идентификатором 1 удален");
    }

    @Test
    public void deleteAll() {
        given(authorService.deleteAll())
                .willReturn(true);
        Assertions.assertThat(sendShellCommand("deleteauthorall")).isEqualTo("таблица авторов очищена");
    }

    public Object sendShellCommand(String command){
        return shell.evaluate(() -> command);
    }
}