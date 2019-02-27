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

import java.sql.Date;
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
                , new Author("Иван", "Бунин", Date.valueOf("1870-10-20"))
                , new Genre("Повесть"));
        book.setId(5);
        review = new Review(book,"Иван", "Хорошо");
    }

    @Test
    public void addNewReview() {
        given(reviewService.add(book.getId(), review.getReviewer(), review.getText()))
                .willReturn(10L);
        Assertions.assertThat(sendShellCommand(String.format("newreview %s Иван Хорошо", book.getId())))
                .isEqualTo("Добавлен комментарий с идентификатором 10");
    }

    @Test
    public void testUpdate() {
        given(reviewService.updateById(Long.valueOf("2"), Long.valueOf("4"), "Иван", "Плохо"))
                .willReturn(true);
        Assertions.assertThat(sendShellCommand("updatereview 2 4 Иван Плохо"))
        .isEqualTo("Комментарий с идентификатором 2 изменен");
    }

    @Test
    public void testFindByBook() throws DataNotFoundException {
        given(reviewService.getByBookId(33L))
                .willReturn(Collections.singletonList(review.toString()));
        Assertions.assertThat(sendShellCommand("getreviewsbyb 33"))
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



