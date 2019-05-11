package ru.otus.library.config;

import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import ru.otus.library.domain.Author;
import ru.otus.library.domain.Book;
import ru.otus.library.domain.Genre;
import ru.otus.library.service.AuthorService;
import ru.otus.library.service.BookService;
import ru.otus.library.service.GenreService;

import java.util.List;

public class BookItemWriter implements ItemWriter<Book> {

    @Autowired
    public BookService bookService;
    @Autowired
    public AuthorService authorService;
    @Autowired
    public GenreService genreService;

    @Override
    public void write(List<? extends Book> list) throws Exception {
        list.forEach(b -> {
            Author author = authorService.add(b.getAuthor().getFirstName(),
                                                b.getAuthor().getSecondName(),
                                                    b.getAuthor().getBirthday());
            Genre genre = genreService.add(b.getGenre().getName());
            bookService.add(new Book(b.getTitle(), author, genre));
        });
    }
}
