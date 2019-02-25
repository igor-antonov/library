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

    Long genreId;
    Genre genre;
    Long authorId;
    Author author;
    Long bookId;
    Book book;
    Long reviewId;
    Review review;

    @Before
    public void prepare() throws DataNotFoundException {
        genreId = genreRepository.insert(new Genre("Comedian"));
        genre = tem.find(Genre.class, genreId);
        authorId = authorRepository.insert(new Author("Иван", "Бунин", Date.valueOf("1870-10-20")));
        author = tem.find(Author.class, authorId);
        bookId = bookRepository.insert(new Book("Сказки", author, genre));
        book = bookRepository.getById(bookId);
        reviewId = reviewRepository.insert(new Review(book, "Критик", "норм"));
        review = reviewRepository.getById(reviewId);
    }

    @Test
    public void findById(){
        Assertions.assertThat(reviewRepository.getById(reviewId).getReviewer()).isEqualTo(review.getReviewer());
    }

    @Test
    public void findByBook(){
        Assertions.assertThat(reviewRepository.getByBook(book).get(0).getReviewer()).isEqualTo(review.getReviewer());
    }

    @Test
    public void deleteAll(){
        reviewRepository.deleteAll();
        Assertions.assertThat(reviewRepository.getAll().size()).isEqualTo(0);
        reviewId = reviewRepository.insert(new Review(book, review.getReviewer(), review.getText()));
        review = tem.find(Review.class, reviewId);
    }

    public void count(){
        Assertions.assertThat(reviewRepository.count()).isEqualTo(1);
    }

    @Test
    public void deleteReviewByBookTitle(){
        bookRepository.deleteByTitle(book.getTitle());
        Assertions.assertThat(reviewRepository.getById(reviewId)).isEqualTo(null);
        reviewId = reviewRepository.insert(new Review(book, review.getReviewer(), review.getText()));
        review = tem.find(Review.class, reviewId);
        bookId = bookRepository.insert(book);
        book = tem.find(Book.class, bookId);
    }

    @Test
    public void deleteReviewByBook(){
        bookRepository.deleteAll();
        Assertions.assertThat(reviewRepository.getById(reviewId)).isEqualTo(null);
        reviewId = reviewRepository.insert(new Review(book, review.getReviewer(), review.getText()));
        review = tem.find(Review.class, reviewId);
        bookId = bookRepository.insert(book);
        book = tem.find(Book.class, bookId);
    }

    @Test
    public void update(){
        review.setText("меняю свое мнение");
        reviewRepository.updateById(reviewId, review);
        Assertions.assertThat(tem.find(Review.class, reviewId).getText()).isEqualTo(review.getText());
    }
}
