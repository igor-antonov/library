package ru.otus.library.service;

import org.springframework.stereotype.Service;
import ru.otus.library.exception.DataNotFoundException;
import ru.otus.library.repository.AuthorRepository;
import ru.otus.library.repository.BookRepository;
import ru.otus.library.repository.GenreRepository;
import ru.otus.library.domain.Author;
import ru.otus.library.domain.Book;
import ru.otus.library.domain.Genre;

import javax.persistence.NoResultException;
import java.util.Collections;
import java.util.List;
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
        Author author = authorRepository.getById(authorId);
        if (author == null){
            throw new DataNotFoundException(String.format("Автор по идентификатору %s не найден", authorId));
        }
        Genre genre = genreRepository.getById(genreId);
        if (genre == null){
            throw new DataNotFoundException(String.format("Жанр по идентификатору %s не найден", genreId));
        }
        return bookRepository.insert(new Book(title, author, genre));
    }

    public boolean updateById(long bookId, String title, long authorId, long genreId) throws DataNotFoundException {
        Author author = authorRepository.getById(authorId);
        if (author == null){
            throw new DataNotFoundException(String.format("Автор по идентификатору %s не найден", authorId));
        }
        Genre genre = genreRepository.getById(genreId);
        if (genre == null){
            throw new DataNotFoundException(String.format("Жанр по идентификатору %s не найден", genreId));
        }
        try {
            bookRepository.getById(bookId);
        }
        catch (DataNotFoundException e)
        {
            throw new DataNotFoundException(String.format("Книга по идентификатору %s не найдена", bookId));
        }
        return bookRepository.updateById(bookId, new Book(title, author, genre));
    }

    public boolean deleteByTitle(String title){
        try {
            return bookRepository.deleteByTitle(title);
        }
        catch (NoResultException e){
            return false;
        }
    }

    public boolean deleteAll(){
        return bookRepository.deleteAll();
    }

    public List<String> getByTitle(String title) throws DataNotFoundException {
        List<String> result = bookRepository.getByTitle(title)
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
        Author author = authorRepository.getById(authorId);
        if (author == null){
            return Collections.singletonList(String.format("Автор по идентификатору %d не найден", authorId));
        }

        List<String> result = bookRepository.getByAuthor(author)
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
        Genre genre = genreRepository.getByName(genreName);
        if (genre == null){
            return Collections.singletonList(String.format("Жанр %s не найден", genreName));
        }

        List<String> result = bookRepository.getByGenre(genre)
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
