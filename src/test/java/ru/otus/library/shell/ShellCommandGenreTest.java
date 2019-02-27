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
import ru.otus.library.domain.Genre;
import ru.otus.library.repository.GenreRepository;
import ru.otus.library.service.GenreService;

import static org.mockito.BDDMockito.given;

@RunWith(SpringRunner.class)
@SpringBootTest(properties={
        InteractiveShellApplicationRunner.SPRING_SHELL_INTERACTIVE_ENABLED + "=false",
        ScriptShellApplicationRunner.SPRING_SHELL_SCRIPT_ENABLED + "=false"
})

public class ShellCommandGenreTest {

    @Autowired
    Shell shell;
    @MockBean
    GenreService genreService;

    @Test
    public void addNewGenre(){
        given(genreService.add("антиутопия")).willReturn(1L);
        Assertions.assertThat(sendCommand("newgenre антиутопия"))
                .isEqualTo("Добавлен жанр антиутопия с идентификатором 1");
    }

    @Test
    public void testCount() {
        given(genreService.getCount()).willReturn(1L);
        Assertions.assertThat(sendCommand("getcountgenres")).isEqualTo("Количество жанров: 1");
    }

    @Test
    public void testUpdate() {
        given(genreService.update("антиутопия", "повесть"))
                .willReturn(true);

        Assertions.assertThat(sendCommand("updategenre антиутопия повесть"))
                .isEqualTo("Жанр антиутопия изменен на повесть");
    }

    @Test
    public void testFindByName() {
        Genre genre = new Genre("антиутопия");
        given(genreService.getByName(genre.getName()))
                .willReturn(genre.toString());

        Assertions.assertThat(sendCommand("getgenresbyname антиутопия"))
                .isEqualTo(genre.toString());
    }

    @Test
    public void deleteAll() {
        given(genreService.deleteAll()).willReturn(true);
        Assertions.assertThat(sendCommand("deletegenreall"))
                .isEqualTo("таблица авторов очищена");
    }
    public Object sendCommand(String command){
        return shell.evaluate(() -> command);
    }

}



