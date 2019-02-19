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
    public void addNewReviewBookNotFound() {
        Mockito.when(reviewRepository.insert(review))
                .thenReturn(-1L);
        Assertions.assertThat(reviewService.add(77L, review.getReviewer(), review.getText()))
                .isEqualTo(-1L);
    }

    @Test
    public void testUpdate() {
        Mockito.when(reviewRepository.updateById(Long.valueOf("7"), review))
                .thenReturn(false);
        Assertions.assertThat(reviewService.updateById(review.getId(), 77L, "Иван", "Плохо"))
                .isEqualTo(false);
    }

    @Test
    public void testNotFoundByBook() throws DataNotFoundException {
        Mockito.when(reviewRepository.getByBook(book)).thenReturn(Collections.singletonList(review));
        Assertions.assertThat(reviewService.getByBookId(book.getId()))
                .isEqualTo(Collections.singletonList(
                        String.format("Книга по идентификатору %d не найдена", book.getId())));
    }

    @Test
    public void deleteAll() {
        Mockito.when(reviewRepository.deleteAll()).thenReturn(true);
        Assertions.assertThat(reviewService.deleteAll())
                .isEqualTo(true);
    }
}