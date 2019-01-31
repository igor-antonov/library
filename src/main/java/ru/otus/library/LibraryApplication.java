package ru.otus.library;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import ru.otus.library.dao.AuthorDAO;
import ru.otus.library.dao.BookDAO;
import ru.otus.library.dao.GenreDAO;
import ru.otus.library.domain.Author;
import ru.otus.library.domain.Genre;

import java.sql.Date;

@SpringBootApplication
public class LibraryApplication {

    public static void main(String[] args) {
        SpringApplication.run(LibraryApplication.class, args);
        /*GenreDAO genreDAO = context.getBean(GenreDAO.class);
        AuthorDAO authorDAO = context.getBean(AuthorDAO.class);
        BookDAO bookDAO = context.getBean(BookDAO.class);
        //Genre genre1 = new Genre(1, "Detective");
        //System.out.println(genreDAO.insert(genre1));
        Genre genre2 = new Genre("Comedian");
        //genreDAO.deleteByName("Comedian");
        genreDAO.insert(genre2);
        genreDAO.insert(new Genre("Comedian"));
        System.out.println("qty: " + genreDAO.count());
        System.out.println(genreDAO.getById(2).toString());
        System.out.println(genreDAO.getByName("Comedian"));
        genreDAO.updateByName("Comedian", new Genre("Detective"));
        for (Genre genre:
             genreDAO.getAll()) {
            System.out.println("foreach: " + genre.toString());

        }

        authorDAO.insert(new Author("Bernard", "Show", Date.valueOf("1920-11-01")));
        System.out.println("authorDAO.count() = " + authorDAO.count());
        System.out.println(authorDAO.getAll());
        System.out.println(authorDAO.getById(2).toString());
        System.out.println(authorDAO.getByBirthday(Date.valueOf("1925-05-03")).toString());
        System.out.println(authorDAO.getByFirstNameAndSecondName("Bernard", "Show").toString());
        authorDAO.updateBySecondName("Show",
                new Author("Alexandr", "Pushkin", Date.valueOf("1920-11-02")));
        System.out.println(authorDAO.getAll());
        authorDAO.deleteBySecondName("Pushkin");
        System.out.println(authorDAO.getAll());

        System.out.println(genre2.getName());
        System.out.println(bookDAO.getByGenre(genre2));

*/
    }
}

