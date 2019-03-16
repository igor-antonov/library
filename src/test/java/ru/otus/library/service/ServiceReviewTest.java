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

import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;

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
                , new Author("Иван", "Бунин", LocalDate.of(1870,10,20))
                , new Genre("Повесть"));
        book.setId("5c8cc0c8c59f940d10471c42");
        review = new Review(book,"Иван", "Хорошо");
        review.setId("5c8d08d7c59f941d200d8148");
    }

    @Test
    public void addNewReview() {
        Review reviewNew = new Review(book,"Петр", "Написал");
        reviewNew.setId("5c8d08d7c59f941d200d8111");
        Mockito.when(bookRepository.findById(book.getId())).thenReturn(Optional.ofNullable(book));
        Mockito.when(reviewRepository.insert(reviewNew))
                .thenReturn(reviewNew);
        Assertions.assertThat(reviewService.add(book.getId(), reviewNew))
                .isEqualTo(reviewNew.getId());
    }

    @Test
    public void testFindByBook() throws DataNotFoundException {
        Mockito.when(bookRepository.findById(book.getId())).thenReturn(Optional.ofNullable(book));
        Mockito.when(reviewRepository.findByBook(book)).thenReturn(Collections.singletonList(review));
        Assertions.assertThat(reviewService.getByBookId(book.getId()).size())
                .isGreaterThan(0);
    }

    @Test
    public void deleteAll() {
        Assertions.assertThat(reviewService.deleteAll())
                .isEqualTo(true);
    }
}