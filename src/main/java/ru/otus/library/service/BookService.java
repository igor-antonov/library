package ru.otus.library.service;

import org.springframework.stereotype.Service;
import ru.otus.library.repository.AuthorRepository;
import ru.otus.library.repository.BookRepository;
import ru.otus.library.repository.GenreRepository;
import ru.otus.library.domain.Author;
import ru.otus.library.domain.Book;
import ru.otus.library.domain.Genre;

import javax.persistence.NoResultException;
import java.util.ArrayList;
import java.util.List;

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

    public long add(String title, long authorId, long genreId){
        Author author = authorRepository.getById(authorId);
        if (author == null){
            return -1;
        }
        Genre genre = genreRepository.getById(genreId);
        if (genre == null){
            return -2;
        }
        return bookRepository.insert(new Book(title, author, genre));
    }

    public long updateById(long bookId, String title, long authorId, long genreId){
        Author author = authorRepository.getById(authorId);
        if (author == null){
            return -1;
        }
        Genre genre = genreRepository.getById(genreId);
        if (genre == null){
            return -2;
        }
        if (bookRepository.getById(bookId) == null) {
            return -3;
        }
        bookRepository.updateById(bookId, new Book(title, author, genre));
        return 1;
    }

    public boolean deleteByTitle(String title){
        try {
            bookRepository.deleteByTitle(title);
            return true;
        }
        catch (NoResultException e){
            return false;
        }
    }

    public boolean deleteAll(){
        bookRepository.deleteAll();
        return true;
    }

    public List<String> getByTitle(String title){
        ArrayList<String> books = new ArrayList<>();
        for (Book book: bookRepository.getByTitle(title)){
            books.add(book.toString());
        }
        if (books.size()>0){
            return books;
        }
        else {
            return null;
        }
    }

    public List<String> getByAuthorId(long authorId){
        ArrayList<String> books = new ArrayList<>();
        Author author = authorRepository.getById(authorId);
        if (author == null){
            books.add(String.format("Автор по идентификатору %d не найден", authorId));
            return books;
        }
        for (Book book: bookRepository.getByAuthor(author)){
            books.add(book.toString());
        }
        if (books.size()>0){
            return books;
        }
        else {
            return null;
        }
    }

    public List<String> getByGenreName(String genreName){
        ArrayList<String> books = new ArrayList<>();
        Genre genre = genreRepository.getByName(genreName);
        if (genre == null){
            books.add(String.format("Жанр %s не найден", genreName));
            return books;
        }

        for (Book book: bookRepository.getByGenre(genre)){
            books.add(book.toString());
        }
        if (books.size()>0){
            return books;
        }
        else {
            return null;
        }
    }
}
