package ru.otus.library;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringRunner;
import ru.otus.library.domain.Book;
import ru.otus.library.domain.Review;
import ru.otus.library.repository.AuthorRepository;
import ru.otus.library.repository.BookRepository;
import ru.otus.library.repository.GenreRepository;
import ru.otus.library.domain.Author;
import ru.otus.library.domain.Genre;
import ru.otus.library.repository.ReviewRepository;
import java.sql.Date;

@RunWith(SpringRunner.class)
@ComponentScan
@DataJpaTest
public class TestsEM {

    @Autowired
    AuthorRepository authorRepository;
    @Autowired
    GenreRepository genreRepository;
    @Autowired
    BookRepository bookRepository;
    @Autowired
    ReviewRepository reviewRepository;
    @Autowired
    TestEntityManager tem;

    private long genreId;
    private long authorId;
    @Before
    public void prepare(){
        authorId = tem.persistAndGetId(
                new Author("Ivanov", "Ivan", Date.valueOf("2000-01-01")),
                Long.class);
        genreId = tem.persistAndGetId(
                new Genre("comedian"), Long.class);

        long bookId = tem.persistAndGetId(
                new Book("tales",
                        tem.find(Author.class, authorId),
                        tem.find(Genre.class, genreId)
                )
                , Long.class
        );
    }

    @Test
    public void deleteReviewByBook(){
        long bookId2 = tem.persistAndGetId(
                new Book("tales",
                        tem.find(Author.class, authorId),
                        tem.find(Genre.class, genreId)
                )
                , Long.class
        );
        long reviewId1 = tem.persistAndGetId(
                new Review(
                        tem.find(Book.class, bookId2),
                        "Петров", "хорошая книга"), Long.class);
        long reviewId2 = tem.persistAndGetId(
                new Review(
                        tem.find(Book.class, bookId2),
                        "Сидоров", "у автора были и получше"), Long.class);
        tem.refresh(tem.find(Book.class, bookId2));
        tem.remove(tem.find(Book.class, bookId2));
        Assertions.assertThat(tem.find(Review.class, reviewId1)).isEqualTo(null);
        Assertions.assertThat(tem.find(Review.class, reviewId2)).isEqualTo(null);
    }
}

