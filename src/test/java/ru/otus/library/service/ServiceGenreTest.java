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
import ru.otus.library.domain.Genre;
import ru.otus.library.repository.GenreRepository;

import static org.mockito.BDDMockito.given;

@RunWith(SpringRunner.class)
@SpringBootTest(properties={
        InteractiveShellApplicationRunner.SPRING_SHELL_INTERACTIVE_ENABLED + "=false",
        ScriptShellApplicationRunner.SPRING_SHELL_SCRIPT_ENABLED + "=false"
})
public class ServiceGenreTest {

    @Autowired
    GenreService genreService;
    @MockBean
    GenreRepository genreRepository;

    private Genre genre;

    @Before
    public void prepare(){
        genre = new Genre("антиутопия");
        genre.setId(7L);
    }

    @Test
    public void addNewGenre(){
        given(genreRepository.insert(genre)).willThrow(NullPointerException.class);
        Assertions.assertThat(genreService.add("антиутопия"))
                .isEqualTo(0L);
    }

    @Test
    public void testCount() {
        given(genreRepository.count()).willReturn(1L);
        Assertions.assertThat(genreService.getCount()).isEqualTo(1L);
    }

    @Test
    public void testUpdate() {
        given(genreRepository.updateByName("антиутопия", "повесть"))
                .willReturn(true);

        Assertions.assertThat(genreService.update("антиутопия", "повесть"))
                .isEqualTo(true);
    }

    @Test
    public void testFindByName() {
        given(genreRepository.getByName(genre.getName()))
                .willReturn(genre);

        Assertions.assertThat(genreService.getByName(genre.getName()))
                .isEqualTo(genre.toString());
    }

    @Test
    public void deleteAll() {
        given(genreRepository.deleteAll()).willReturn(true);
        Assertions.assertThat(genreService.deleteAll())
                .isEqualTo(true);
    }
}