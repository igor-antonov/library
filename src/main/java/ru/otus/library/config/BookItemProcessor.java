package ru.otus.library.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import ru.otus.library.domain.Author;
import ru.otus.library.domain.Book;
import ru.otus.library.domain.BookMongo;
import ru.otus.library.domain.Genre;
import ru.otus.library.service.AuthorService;
import ru.otus.library.service.BookService;
import ru.otus.library.service.GenreService;

public class BookItemProcessor implements ItemProcessor<BookMongo, Book> {

    @Autowired
    public BookService bookService;
    @Autowired
    public AuthorService authorService;
    @Autowired
    public GenreService genreService;

    private final Logger log = LoggerFactory.getLogger(BookItemProcessor.class);

    @Override
    public Book process(final BookMongo bookMongo) throws Exception {
        Author author = authorService.getByFirstNameAndSecondNameAndBirthday(bookMongo.getAuthor().getFirstName(),
                bookMongo.getAuthor().getSecondName(),
                bookMongo.getAuthor().getBirthday());
        Genre genre = genreService.getByName(bookMongo.getGenre().getName());

        Book book = new Book(bookMongo.getTitle(), author, genre);
        bookService.add(book);
        log.info("Converting (" + bookMongo + ") into (" + book + ")");
        return book;
    }
}
