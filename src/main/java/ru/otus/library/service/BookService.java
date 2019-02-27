package ru.otus.library.service;

import org.springframework.stereotype.Service;
import ru.otus.library.exception.DataNotFoundException;
import ru.otus.library.repository.AuthorRepository;
import ru.otus.library.repository.BookRepository;
import ru.otus.library.repository.GenreRepository;
import ru.otus.library.domain.Author;
import ru.otus.library.domain.Book;
import ru.otus.library.domain.Genre;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookService {

    private final BookRepository bookRepository;
    private final GenreRepository genreRepository;
    private final AuthorRepository authorRepository;

    public BookService(BookRepository bookRepository, GenreRepository genreRepository, AuthorRepository authorRepository) {
        this.bookRepository = bookRepository;
        this.genreRepository = genreRepository;
        this.authorRepository = authorRepository;
    }

    public long add(String title, long authorId, long genreId) throws DataNotFoundException {
        Optional<Author> authorOpt = authorRepository.findById(authorId);
        if (!authorOpt.isPresent()){
            throw new DataNotFoundException(String.format("Автор по идентификатору %s не найден", authorId));
        }
        Optional<Genre> genreOpt = genreRepository.findById(genreId);
        if (!genreOpt.isPresent()){
            throw new DataNotFoundException(String.format("Жанр по идентификатору %s не найден", genreId));
        }
        return bookRepository.save(
                new Book(title, authorOpt.get(), genreOpt.get())).getId();
    }

    public boolean updateById(long bookId, String title, long authorId, long genreId) throws DataNotFoundException {
        Optional<Author> authorOpt = authorRepository.findById(authorId);
        if (!authorOpt.isPresent()){
            throw new DataNotFoundException(String.format("Автор по идентификатору %s не найден", authorId));
        }
        Optional<Genre> genreOpt = genreRepository.findById(genreId);
        if (!genreOpt.isPresent()){
            throw new DataNotFoundException(String.format("Жанр по идентификатору %s не найден", genreId));
        }
        Optional<Book> bookOpt = bookRepository.findById(bookId);
        if (!bookOpt.isPresent()){
            throw new DataNotFoundException(String.format("Книга по идентификатору %s не найдена", bookId));
        }
        return bookRepository.updateById(bookId,
                new Book(title, authorOpt.get(), genreOpt.get())) > 0;
    }

    public boolean deleteByTitle(String title){
        bookRepository.deleteByTitle(title);
        return bookRepository.deleteByTitle(title) > 0;
    }

    public boolean deleteAll(){
        bookRepository.deleteAll();
        return true;
    }

    public List<String> getByTitle(String title) throws DataNotFoundException {
        List<String> result = bookRepository.findByTitle(title)
                .stream().
                        map(Book::toString)
                .collect(Collectors.toList());
        if (result.isEmpty()){
            throw new DataNotFoundException("Результаты не найдены");
        }
        else {
            return result;
        }
    }

    public List<String> getByAuthorId(long authorId) throws DataNotFoundException {
        Optional<Author> authorOpt = authorRepository.findById(authorId);
        if (!authorOpt.isPresent()){
            return Collections.singletonList(String.format("Автор по идентификатору %d не найден", authorId));
        }

        List<String> result = bookRepository.findByAuthor(authorOpt.get())
                .stream().
                        map(Book::toString)
                .collect(Collectors.toList());
        if (result.isEmpty()){
            throw new DataNotFoundException("Результаты не найдены");
        }
        else {
            return result;
        }
    }

    public List<String> getByGenreName(String genreName) throws DataNotFoundException {
        Optional<Genre> genreOpt = genreRepository.findByName(genreName);
        if (!genreOpt.isPresent()){
            return Collections.singletonList(String.format("Жанр %s не найден", genreName));
        }

        List<String> result = bookRepository.findByGenre(genreOpt.get())
                .stream().
                        map(Book::toString)
                .collect(Collectors.toList());
        if (result.isEmpty()){
            throw new DataNotFoundException("Результаты не найдены");
        }
        else {
            return result;
        }
    }
}
