package ru.otus.library.service;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.shell.jline.InteractiveShellApplicationRunner;
import org.springframework.shell.jline.ScriptShellApplicationRunner;
import org.springframework.test.context.junit4.SpringRunner;
import ru.otus.library.domain.Author;
import ru.otus.library.domain.Book;
import ru.otus.library.domain.Genre;
import ru.otus.library.domain.Review;
import ru.otus.library.exception.DataNotFoundException;
import ru.otus.library.repository.BookRepository;
import ru.otus.library.repository.ReviewRepository;

import java.sql.Date;
import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RunWith(SpringRunner.class)
@SpringBootTest(properties={
        InteractiveShellApplicationRunner.SPRING_SHELL_INTERACTIVE_ENABLED + "=false",
        ScriptShellApplicationRunner.SPRING_SHELL_SCRIPT_ENABLED + "=false"
})
public class ServiceReviewTest {

    @Autowired
    ReviewService reviewService;
    @MockBean
    ReviewRepository reviewRepository;
    @MockBean
    BookRepository bookRepository;

    private Review review;
    private Book book;

    @Before
    public void prepare(){
        book = new Book("Вий"
                , new Author("Иван", "Бунин", Date.valueOf("1870-10-20"))
                , new Genre("Повесть"));
        book.setId(5L);
        review = new Review(book,"Иван", "Хорошо");
        review.setId(7L);
    }

    @Test
    public void addNewReview() throws DataNotFoundException {
        Mockito.when(bookRepository.getById(book.getId())).thenReturn(book);
        Mockito.when(reviewRepository.insert(review))
                .thenReturn(0L);
        Assertions.assertThat(reviewService.add(book.getId(), review.getReviewer(), review.getText()))
                .isEqualTo(0L);
    }

    @Test
    public void testFailureUpdate() {
        Mockito.when(reviewRepository.updateById(Long.valueOf("7"), review))
                .thenReturn(false);
        Assertions.assertThat(reviewService.updateById(1, 77L, "Иван", "Плохо"))
                .isEqualTo(false);
    }

    @Test
    public void testFoundByBook() throws DataNotFoundException {
        Mockito.when(bookRepository.getById((book.getId()))).thenReturn(book);
        Mockito.when(reviewRepository.getByBook(book)).thenReturn(Collections.singletonList(review));
        Assertions.assertThat(reviewService.getByBookId(book.getId()))
                .isEqualTo(Stream.of(review).
                map(Review::toString)
                .collect(Collectors.toList()));
    }

    @Test
    public void deleteAll() {
        Mockito.when(reviewRepository.deleteAll()).thenReturn(true);
        Assertions.assertThat(reviewService.deleteAll())
                .isEqualTo(true);
    }
}