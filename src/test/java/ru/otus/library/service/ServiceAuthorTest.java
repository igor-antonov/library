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
import ru.otus.library.exception.DataNotFoundException;
import ru.otus.library.repository.AuthorRepository;

import java.sql.Date;
import java.util.Collections;

import static org.mockito.BDDMockito.given;

@RunWith(SpringRunner.class)
@SpringBootTest(properties={
        InteractiveShellApplicationRunner.SPRING_SHELL_INTERACTIVE_ENABLED + "=false",
        ScriptShellApplicationRunner.SPRING_SHELL_SCRIPT_ENABLED + "=false"
})
public class ServiceAuthorTest {

    @Autowired
    AuthorService authorService;
    @MockBean
    AuthorRepository authorRepository;

    private Author author;

    @Before
    public void prepare(){
        author = new Author("Иван", "Бунин", Date.valueOf("1870-10-20"));
        author.setId(5L);
    }

    @Test
    public void testCount() {
        given(authorRepository.count())
                .willReturn(2L);
        Assertions.assertThat(authorService.getCount()).isEqualTo(2L);
    }

    @Test
    public void addNewAuthor() {
        given(authorRepository.insert(author))
                .willReturn(author.getId());
        Assertions.assertThat(authorService.add("Иван", "Бунин", Date.valueOf("1870-10-20")))
                .isEqualTo(0L);
    }

    @Test
    public void updateAuthor() throws DataNotFoundException {
        given(authorRepository.updateBySecondName("Бунин",
                "Николай", "Гоголь", Date.valueOf("1809-03-20")))
                .willReturn(true);
        Assertions.assertThat(authorService.updateBySecondName("Бунин",
                "Николай", "Гоголь", Date.valueOf("1809-03-20")))
                .isEqualTo(true);
    }

    @Test
    public void findBySecondName() throws DataNotFoundException {
        given(authorRepository.getBySecondName("Бунин"))
                .willReturn(Collections.singletonList(author));
        Assertions.assertThat(authorService.getBySecondName("Бунин"))
                .isEqualTo(Collections.singletonList(author.toString()));
    }

    @Test
    public void deleteAuthorById(){
        given(authorRepository.deleteById(1)).willReturn(true);
        Assertions.assertThat(authorService.deleteById(1))
                .isEqualTo(true);
    }

    @Test
    public void deleteAll() {
        given(authorRepository.deleteAll())
                .willReturn(true);
        Assertions.assertThat(authorService.deleteAll()).isEqualTo(true);
    }
}