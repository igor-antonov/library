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
import ru.otus.library.domain.Author;

import java.sql.Date;
import java.util.Collections;

@RunWith(SpringRunner.class)
@ComponentScan
@DataJpaTest
public class AuthorRepositoryTest {

    @Autowired
    AuthorRepository authorRepository;
    @Autowired
    TestEntityManager tem;

    private Author author;
    private Long authorId;

    @Before
    public void prepare(){
        authorId = authorRepository.save(new Author("Иван", "Бунин", Date.valueOf("1870-10-20"))).getId();
        author = tem.find(Author.class, authorId);
    }

    @Test
    public void findById(){
        Assertions.assertThat(authorRepository.findById(authorId).get().getSecondName()).isEqualTo(author.getSecondName());
    }

    @Test
    public void findByFirstNameAndSecondName(){
        Assertions.assertThat(authorRepository.findByFirstNameAndSecondName(
                author.getFirstName(), author.getSecondName()
        ).get(0).getSecondName()).isEqualTo(author.getSecondName());
    }

    @Test
    public void findBySecondName(){
        Assertions.assertThat(authorRepository.findBySecondName(
                author.getSecondName()
        ).get(0).getSecondName()).isEqualTo(author.getSecondName());
    }

    @Test
    public void findByBirthday(){
        Assertions.assertThat(authorRepository.findByBirthday(
                author.getBirthday()
        ).get(0).getSecondName()).isEqualTo(author.getSecondName());
    }

    @Test
    public void getAll(){
        Assertions.assertThat(authorRepository.findAll()).isEqualTo(Collections.singletonList(author));
    }

    @Test
    public void deleteById(){
        long authorId2 = tem.persistAndGetId(
                new Author("Иван", "Иванов", Date.valueOf("1970-10-20")), Long.class);
        Assertions.assertThat(authorRepository.findById(authorId2).get().getSecondName()).isEqualTo("Иванов");
        authorRepository.deleteById(authorId2);
        Assertions.assertThat(authorRepository.findById(authorId2).isPresent()).isEqualTo(false);
    }

    @Test
    public void deleteAll(){
        authorRepository.deleteAll();
        Assertions.assertThat(authorRepository.count()).isEqualTo(0);
        authorId = authorRepository.save(author).getId();
        author = tem.find(Author.class, authorId);
    }

    @Test
    public void update(){
        authorRepository.updateById(authorId, "Петр", "Петров", Date.valueOf("1980-10-20"));
        author.setFirstName("Петр");
        author.setSecondName("Петров");
        author.setBirthday(Date.valueOf("1980-10-20"));
        Assertions.assertThat(tem.find(Author.class, authorId).getSecondName()).isEqualTo(author.getSecondName());
    }
}
