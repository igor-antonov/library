package ru.otus.library.shell;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.shell.Shell;
import org.springframework.shell.jline.InteractiveShellApplicationRunner;
import org.springframework.shell.jline.ScriptShellApplicationRunner;
import org.springframework.test.context.junit4.SpringRunner;
import ru.otus.library.domain.Author;
import ru.otus.library.domain.Book;
import ru.otus.library.domain.Genre;
import ru.otus.library.domain.Review;
import ru.otus.library.exception.DataNotFoundException;
import ru.otus.library.service.ReviewService;

import static org.mockito.BDDMockito.given;

import java.time.LocalDate;
import java.util.Collections;

@RunWith(SpringRunner.class)
@SpringBootTest(properties={
        InteractiveShellApplicationRunner.SPRING_SHELL_INTERACTIVE_ENABLED + "=false",
        ScriptShellApplicationRunner.SPRING_SHELL_SCRIPT_ENABLED + "=false"
})
public class ShellCommandReviewTest {

    @Autowired
    Shell shell;
    @MockBean
    ReviewService reviewService;

    private Review review;
    private Book book;

    @Before
    public void prepare(){
        book = new Book("Вий"
                , new Author("Иван", "Бунин", LocalDate.of(1870,10,20))
                , new Genre("Повесть"));
        book.setId("5c8cc0c8c59f940d10471c42");
        review = new Review(book,"Иван", "Хорошо");
    }

    @Test
    public void addNewReview() {
        given(reviewService.add(book.getId(), review.getReviewer(), review.getText()))
                .willReturn("5c8d08d7c59f941d200d8148");
        Assertions.assertThat(sendShellCommand(String.format("newreview %s Иван Хорошо", book.getId())))
                .isEqualTo("Добавлен комментарий с идентификатором 5c8d08d7c59f941d200d8148");
    }

    @Test
    public void testUpdate() {
        Assertions.assertThat(sendShellCommand("updatereview 2 4 Иван Плохо"))
        .isEqualTo("Комментарий с идентификатором 2 изменен");
    }

    @Test
    public void testFindByBook() throws DataNotFoundException {
        given(reviewService.getByBookId(book.getId()))
                .willReturn(Collections.singletonList(review.toString()));
        Assertions.assertThat(sendShellCommand("getreviewsbyb 5c8cc0c8c59f940d10471c42"))
                .isEqualTo(Collections.singletonList(review.toString()));
    }

    @Test
    public void deleteAll() {
        given(reviewService.deleteAll()).willReturn(true);
        Assertions.assertThat(sendShellCommand("deletereviewall"))
        .isEqualTo("Таблица рецензий очищена");
    }

    public Object sendShellCommand(String command){
        return shell.evaluate(() -> command);
    }
}



