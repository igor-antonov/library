package ru.otus.library.repository;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringRunner;
import ru.otus.library.domain.Author;
import ru.otus.library.domain.Book;
import ru.otus.library.domain.Genre;
import ru.otus.library.domain.Review;

import java.time.LocalDate;

@RunWith(SpringRunner.class)
@ComponentScan
@DataMongoTest
public class ReviewRepositoryTest {

    @Autowired
    BookRepository bookRepository;
    @Autowired
    ReviewRepository reviewRepository;

    private Book book;
    private String reviewId;
    private Review review;

    @Before
    public void prepare() {
        Genre genre = new Genre("Comedian");
        Author author = new Author("Иван", "Бунин", LocalDate.of(1870,10,20));
        book = bookRepository.insert(new Book("Сказки", author, genre));
        reviewId = reviewRepository.insert(new Review(book, "Критик", "норм")).getId();
        review = reviewRepository.findById(reviewId).get();
    }

    @Test
    public void findById(){
        Assertions.assertThat(reviewRepository.findById(reviewId).get().getReviewer())
                .isEqualTo(review.getReviewer());
    }

    @Test
    public void findByBook(){
        Assertions.assertThat(reviewRepository.findByBook(book).get(0).getReviewer())
                .isEqualTo(review.getReviewer());
    }

    @Test
    public void deleteAll(){
        reviewRepository.deleteAll();
        System.out.println(bookRepository.findAll());
        Assertions.assertThat(reviewRepository.findAll().size()).isEqualTo(0);
        reviewId = reviewRepository.insert(new Review(book, review.getReviewer(), review.getText())).getId();
        review = reviewRepository.findById(reviewId).get();
    }

    @Test
    public void count(){
        Assertions.assertThat(reviewRepository.count()).isGreaterThanOrEqualTo(1);
    }

    @Test
    public void update(){
        review.setText("меняю свое мнение");
        reviewRepository.save(review);
        Assertions.assertThat(reviewRepository.findById(reviewId).get().getText())
                .isEqualTo(review.getText());
    }
}
