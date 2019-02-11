package ru.otus.library;

import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.shell.Shell;
import org.springframework.shell.jline.InteractiveShellApplicationRunner;
import org.springframework.shell.jline.ScriptShellApplicationRunner;
import org.springframework.test.context.junit4.SpringRunner;
import ru.otus.library.domain.Book;
import ru.otus.library.domain.Review;
import ru.otus.library.repository.AuthorRepository;
import ru.otus.library.repository.BookRepository;
import ru.otus.library.repository.GenreRepository;
import ru.otus.library.domain.Author;
import ru.otus.library.domain.Genre;
import ru.otus.library.repository.ReviewRepository;
import ru.otus.library.shell.AuthorCommand;
import ru.otus.library.shell.BookCommand;
import ru.otus.library.shell.GenreCommand;

import java.sql.Date;

@RunWith(SpringRunner.class)
@SpringBootTest(properties={
        InteractiveShellApplicationRunner.SPRING_SHELL_INTERACTIVE_ENABLED + "=false",
        ScriptShellApplicationRunner.SPRING_SHELL_SCRIPT_ENABLED + "=false"
})

public class ShellCommandAndRepositoryTest {

    @Autowired
    AuthorCommand authorCommand;
    @Autowired
    BookCommand bookCommand;
    @Autowired
    GenreCommand genreCommand;
    @Autowired
    AuthorRepository authorRepository;
    @Autowired
    GenreRepository genreRepository;
    @Autowired
    BookRepository bookRepository;
    @Autowired
    ReviewRepository reviewRepository;
    @Autowired
    Shell shell;

    private Author author;
    private Genre genre;
    private String bookId;
    private String genreId;
    private String authorId;

    @Before
    public void contextLoads() {

        author = new Author("Джордж", "Оруэлл", Date.valueOf("1903-06-25"));
        genre = new Genre("антиутопия");
        String shellAuthorId = shell.evaluate(() -> String.format("newauthor %s %s %s",
                author.getFirstName(), author.getSecondName(), author.getBirthday().toString())).toString();
        authorId = shellAuthorId.substring(shellAuthorId.lastIndexOf(" ") + 1);
        String shellGenreId = shell.evaluate(() -> "newgenre " + genre.getName()).toString();
        genreId = shellGenreId.substring(shellGenreId.lastIndexOf(" ") + 1);
        String shellBookId = shell.evaluate(() ->
                String.format("newbook 1984 %s %s", authorId, genreId)).toString();
        bookId = shellBookId.substring(shellBookId.lastIndexOf(" ") + 1);
    }

    @Test
    public void testFindById() {
        Assertions.assertThat(authorRepository.getById(Integer.valueOf(authorId))).isNotEqualTo(null);
        Assertions.assertThat(genreRepository.getById(Long.valueOf(genreId))).isNotEqualTo(null);
        Assertions.assertThat(bookRepository.getById(Integer.valueOf(bookId))).isNotEqualTo(null);
    }

    @Test
    public void testCount() {
        Assertions.assertThat(authorRepository.count()).isEqualTo(1);
        Assertions.assertThat(genreRepository.count()).isEqualTo(1);
        Assertions.assertThat(bookRepository.count()).isEqualTo(1);
    }

    @Test
    public void testUpdate() {
        shell.evaluate(() -> String.format("editauthorid %s Николай Гоголь 1809-03-20", authorId));
        shell.evaluate(() -> "updategenre антиутопия повесть");
        shell.evaluate(() -> String.format("editbook %s %s %s %s", bookId, "Вий", authorId, genreId));
        Assertions.assertThat(authorRepository.getBySecondName("Гоголь").size()).isGreaterThan(0);
        Assertions.assertThat(genreRepository.getByName("повесть").getId()).isEqualTo(Long.valueOf(genreId));
        Assertions.assertThat(bookRepository.getByTitle("Вий").size()).isGreaterThan(0);
    }

    @Test
    public void testUpdateDuplicate() {
        String shellAuthorId = shell.evaluate(() -> String.format("newauthor %s %s %s",
                "Иван", "Бунин", Date.valueOf("1870-10-20").toString())).toString();
        String authorId2 = shellAuthorId.substring(shellAuthorId.lastIndexOf(" ") + 1);
        shell.evaluate(() ->"editauthor Бунин Николай Гоголь 1809-03-20");
        Assertions.assertThat(authorRepository.getById(Long.valueOf(authorId2)).getFirstName()).isEqualTo("Николай");
        authorRepository.deleteById(Integer.valueOf(authorId2));
    }

    @Test
    public void deleteReviewByBook() {
        String shellBookId2 = shell.evaluate(() ->
                String.format("newbook Инферно %s %s", authorId, genreId)).toString();
        String bookId2 = shellBookId2.substring(shellBookId2.lastIndexOf(" ") + 1);
        Book book2 = bookRepository.getById(Long.valueOf(bookId2));
        shell.evaluate(() -> "newreview 61 Гагарин понравилось");
        reviewRepository.insert(
                new Review(bookRepository.getById(Long.valueOf(bookId2)), "Гагарин", "понравилось"));
        Assertions.assertThat(reviewRepository.getByBook(book2).size())
                .isEqualTo(2);
        shell.evaluate(() ->"deletebook Инферно");
        Assertions.assertThat(reviewRepository.getByBook(book2).size())
                .isEqualTo(0);
    }

    @After
    public void deleteData() {
     shell.evaluate(() -> "deletebookall");
     shell.evaluate(() -> "deleteauthorall");
     shell.evaluate(() -> "deletegenreall");
     shell.evaluate(() -> "deletereviewall");
    }
}


