package ru.otus.library.repository;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringRunner;
import ru.otus.library.domain.Genre;

import java.util.Collections;

@RunWith(SpringRunner.class)
@ComponentScan
@DataJpaTest
public class GenreRepositoryTest {

    @Autowired
    GenreRepository genreRepository;
    @Autowired
    TestEntityManager tem;

    Long genreId;
    Genre genre;

    @Before
    public void prepare(){
        genreId = genreRepository.insert(new Genre("Comedian"));
        genre = tem.find(Genre.class, genreId);
    }

    @Test
    public void findById(){
        Assertions.assertThat(genreRepository.getById(genreId).getName()).isEqualTo(genre.getName());
    }

    @Test
    public void findByName(){
        Assertions.assertThat(genreRepository.getByName(genre.getName()).getId()).isEqualTo(genreId);
    }

    @Test
    public void getAll(){
        Assertions.assertThat(genreRepository.getAll()).isEqualTo(Collections.singletonList(genre));
    }

    @Test
    public void deleteByName(){
        long genreId2 = tem.persistAndGetId(new Genre("Drama"), Long.class);
        Assertions.assertThat(genreRepository.getById(genreId2).getName()).isEqualTo("Drama");
        genreRepository.deleteByName("Drama");
        Assertions.assertThat(genreRepository.getById(genreId2)).isEqualTo(null);
    }

    @Test
    public void deleteAll(){
        genreRepository.deleteAll();
        Assertions.assertThat(genreRepository.getAll().size()).isEqualTo(0);
        genreId = genreRepository.insert(new Genre(genre.getName()));
        genre = tem.find(Genre.class, genreId);
    }

    @Test
    public void count(){
        Assertions.assertThat(genreRepository.count()).isEqualTo(1);
    }

    @Test
    public void update(){
        genreRepository.updateByName(genre.getName(), "Detective");
        genre.setName("Detective");
        Assertions.assertThat(tem.find(Genre.class, genreId).getName()).isEqualTo(genre.getName());
    }

}
