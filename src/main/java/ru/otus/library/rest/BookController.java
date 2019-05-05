package ru.otus.library.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.otus.library.domain.Author;
import ru.otus.library.domain.Book;
import ru.otus.library.domain.Genre;
import ru.otus.library.exception.DataNotFoundException;
import ru.otus.library.rest.dto.BookDto;
import ru.otus.library.service.AuthorService;
import ru.otus.library.service.BookService;
import ru.otus.library.service.GenreService;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/book")
public class BookController {
    private final Logger log = LoggerFactory.getLogger(BookController.class);
    private final BookService bookService;
    private final AuthorService authorService;
    private final GenreService genreService;

    public BookController(BookService bookService, AuthorService authorService, GenreService genreService) {
        this.bookService = bookService;
        this.authorService = authorService;
        this.genreService = genreService;
    }

    @GetMapping
    public List<BookDto> getAll() {
        return bookService.getAll().stream().map(BookDto::toDto).collect(Collectors.toList());
    }

    @DeleteMapping("{id}")
    public ResponseEntity deleteById(@PathVariable("id") Long bookId) {
        try {
            bookService.deleteById(bookId);
            return new ResponseEntity(HttpStatus.OK);
        }
        catch (DataNotFoundException e){
            log.error(e.getMessage());
            log.error(Arrays.toString(e.getStackTrace()));
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public Book add(@RequestBody Book book) throws DataNotFoundException {
        Author author = authorService.getByFirstNameAndSecondNameAndBirthday(book.getAuthor().getFirstName(),
                                                            book.getAuthor().getSecondName(),
                                                            book.getAuthor().getBirthday());
        Genre genre = genreService.getByName(book.getGenre().getName());
        book.getAuthor().setId(author.getId());
        book.getGenre().setId(genre.getId());
        return bookService.add(book);
    }

    @PutMapping("{id}")
    public Book edit(@PathVariable("id") Long bookId, @RequestBody Book book) throws DataNotFoundException {
        try {
            return bookService.update(bookId, book);
        }
        catch (DataNotFoundException e) {
            log.error(e.getMessage());
            log.error(Arrays.toString(e.getStackTrace()));
            throw new DataNotFoundException(e);
        }
    }
}
