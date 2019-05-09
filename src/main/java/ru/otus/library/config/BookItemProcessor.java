package ru.otus.library.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import ru.otus.library.domain.Author;
import ru.otus.library.domain.Book;
import ru.otus.library.domain.BookMongo;
import ru.otus.library.domain.Genre;

public class BookItemProcessor implements ItemProcessor<BookMongo, Book> {

    private final Logger log = LoggerFactory.getLogger(BookItemProcessor.class);

    @Override
    public Book process(final BookMongo bookMongo) throws Exception {
        Author author = new Author(bookMongo.getAuthor().getFirstName(),
                bookMongo.getAuthor().getSecondName(),
                bookMongo.getAuthor().getBirthday());
        Genre genre = new Genre(bookMongo.getGenre().getName());
        Book book = new Book(bookMongo.getTitle(), author, genre);
        log.info("Converting (" + bookMongo + ") into (" + book + ")");
        return book;
    }
}
