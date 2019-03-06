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
import ru.otus.library.domain.Book;
import ru.otus.library.domain.Genre;
import ru.otus.library.domain.Review;
import ru.otus.library.exception.DataNotFoundException;

import java.sql.Date;

@RunWith(SpringRunner.class)
@ComponentScan
@DataJpaTest
public class ReviewRepositoryTest {

    @Autowired
    GenreRepository genreRepository;
    @Autowired
    AuthorRepository authorRepository;
    @Autowired
    BookRepository bookRepository;
    @Autowired
    ReviewRepository reviewRepository;
    @Autowired
    TestEntityManager tem;

    private Genre genre;
    private Author author;
    private Long bookId;
    private Book book;
    private Long reviewId;
    private Review review;

    @Before
    public void prepare() throws DataNotFoundException {
        genre = genreRepository.save(new Genre("Comedian"));
        Long authorId = authorRepository.save(new Author("Иван", "Бунин", Date.valueOf("1870-10-20"))).getId();
        author = tem.find(Author.class, authorId);
        bookId = bookRepository.save(new Book("Сказки", author, genre)).getId();
        book = bookRepository.findById(bookId).get();
        reviewId = reviewRepository.save(new Review(book, "Критик", "норм")).getId();
        review = reviewRepository.findById(reviewId).get();
    }

    @Test
    public void findById(){
        Assertions.assertThat(reviewRepository.findById(reviewId).get().getReviewer()).isEqualTo(review.getReviewer());
    }

    @Test
    public void findByBook(){
        System.out.println("reviewRepository = " + reviewRepository.findAll());
        System.out.println("bookRepository = " + bookRepository.findAll());
        Assertions.assertThat(reviewRepository.findByBook(book).get(0).getReviewer()).isEqualTo(review.getReviewer());
    }

    @Test
    public void deleteAll(){
        reviewRepository.deleteAll();
        System.out.println(bookRepository.findAll());
        Assertions.assertThat(reviewRepository.findAll().size()).isEqualTo(0);
        reviewId = reviewRepository.save(new Review(book, review.getReviewer(), review.getText())).getId();
        review = tem.find(Review.class, reviewId);
    }

    @Test
    public void count(){
        System.out.println("reviewRepository = " + reviewRepository.findAll());
        Assertions.assertThat(reviewRepository.count()).isEqualTo(1);
    }

    @Test
    public void update(){
        review.setText("меняю свое мнение");
        reviewRepository.updateById(reviewId, review);
        Assertions.assertThat(tem.find(Review.class, reviewId).getText()).isEqualTo(review.getText());
    }
}
