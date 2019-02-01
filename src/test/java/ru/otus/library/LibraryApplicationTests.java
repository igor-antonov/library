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
import ru.otus.library.dao.AuthorDAO;
import ru.otus.library.dao.BookDAO;
import ru.otus.library.dao.GenreDAO;
import ru.otus.library.domain.Author;
import ru.otus.library.domain.Genre;
import ru.otus.library.shell.AuthorCommand;
import ru.otus.library.shell.BookCommand;
import ru.otus.library.shell.GenreCommand;

import java.sql.Date;

@RunWith(SpringRunner.class)
@SpringBootTest(properties={
        InteractiveShellApplicationRunner.SPRING_SHELL_INTERACTIVE_ENABLED + "=false",
        ScriptShellApplicationRunner.SPRING_SHELL_SCRIPT_ENABLED + "=false"
})
public class LibraryApplicationTests {

    @Autowired
    AuthorCommand authorCommand;
    @Autowired
    BookCommand bookCommand;
    @Autowired
    GenreCommand genreCommand;
    @Autowired
    AuthorDAO authorDAO;
    @Autowired
    GenreDAO genreDAO;
    @Autowired
    BookDAO bookDAO;
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
    public void testFindById(){
        Assertions.assertThat(authorDAO.getById(Integer.valueOf(authorId)) != null);
        Assertions.assertThat(genreDAO.getById(Integer.valueOf(genreId)) != null);
        Assertions.assertThat(bookDAO.getById(Integer.valueOf(bookId)) != null);
    }

    @Test
    public void testCount(){
        Assertions.assertThat(authorDAO.count() == 1);
        Assertions.assertThat(genreDAO.count() == 1);
        Assertions.assertThat(bookDAO.count() == 1);
    }

    @Test
    public void testUpdate(){
        shell.evaluate(() -> String.format("editauthorid %s Николай Гоголь 1809-03-20", authorId));
        shell.evaluate(() -> "updategenre антиутопия повесть");
        shell.evaluate(() -> String.format("editbook %s %s %s %s", bookId, "Вий", authorId, genreId));
        Assertions.assertThat(authorDAO.getBySecondName("Гоголь").size()>0);
        Assertions.assertThat(genreDAO.getByName("повесть").getId() == Integer.valueOf(genreId));
        Assertions.assertThat(bookDAO.getByTitle("Вий").size()>0);
    }

    @Test
    public void testUpdateDuplicate(){
        String secondName = authorDAO.getById(Integer.valueOf(authorId)).getSecondName();
        String shellAuthorId = shell.evaluate(() -> String.format("newauthor %s %s %s",
                "Иван", secondName, Date.valueOf("1888-06-25").toString())).toString();
        String authorId2 = shellAuthorId.substring(shellAuthorId.lastIndexOf(" ") + 1);
        shell.evaluate(() -> String.format("editauthor %s Николай Гоголь 1809-03-20", authorId));
        String result = shell.evaluate(() -> String.format("editauthor %s Николай Гоголь 1809-03-20", authorId)).toString();
        authorDAO.deleteById(Integer.valueOf(authorId2));
    }

    @After
    public void deleteData(){
        shell.evaluate(() -> "deletebookall");
        shell.evaluate(() -> "deleteauthorall");
        shell.evaluate(() -> "deletegenreall");
    }

}

