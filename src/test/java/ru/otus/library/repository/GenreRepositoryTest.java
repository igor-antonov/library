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
import java.util.Optional;

@RunWith(SpringRunner.class)
@ComponentScan
@DataJpaTest
public class GenreRepositoryTest {

    @Autowired
    GenreRepository genreRepository;
    @Autowired
    TestEntityManager tem;

    private Long genreId;
    private Genre genre;

    @Before
    public void prepare(){
        genreId = genreRepository.save(new Genre("Comedian")).getId();
        genre = tem.find(Genre.class, genreId);

    }

    @Test
    public void findById(){
        Assertions.assertThat(genreRepository.findById(genreId).get().getName()).isEqualTo(genre.getName());
    }

    @Test
    public void findByName(){
        Assertions.assertThat(genreRepository.findByName(genre.getName()).get().getId()).isEqualTo(genreId);
    }

    @Test
    public void getAll(){
        Assertions.assertThat(genreRepository.findAll()).isEqualTo(Collections.singletonList(genre));
    }

    @Test
    public void deleteByName(){
        long genreId2 = tem.persistAndGetId(new Genre("Drama"), Long.class);
        Assertions.assertThat(genreRepository.findById(genreId2).get().getName()).isEqualTo("Drama");
        genreRepository.deleteByName("Drama");
        Assertions.assertThat(genreRepository.findById(genreId2)).isEqualTo(Optional.empty());
    }

    @Test
    public void deleteAll(){
        genreRepository.deleteAll();
        Assertions.assertThat(genreRepository.findAll().spliterator().estimateSize()).isEqualTo(0);
        genreId = genreRepository.save(new Genre(genre.getName())).getId();
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
