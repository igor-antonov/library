package ru.otus.library.service;

import org.springframework.stereotype.Service;
import ru.otus.library.dao.AuthorDAO;
import ru.otus.library.dao.BookDAO;
import ru.otus.library.dao.GenreDAO;
import ru.otus.library.domain.Author;
import ru.otus.library.domain.Book;
import ru.otus.library.domain.Genre;

import java.util.ArrayList;
import java.util.List;

@Service
public class BookService {

    private final BookDAO bookDAO;
    private final GenreDAO genreDAO;
    private final AuthorDAO authorDAO;

    public BookService(BookDAO bookDAO, GenreDAO genreDAO, AuthorDAO authorDAO) {
        this.bookDAO = bookDAO;
        this.genreDAO = genreDAO;
        this.authorDAO = authorDAO;
    }

    public int add(String title, int authorId, int genreId){
        Author author = authorDAO.getById(authorId);
        if (author == null){
            return -1;
        }
        Genre genre = genreDAO.getById(genreId);
        if (genre == null){
            return -2;
        }
        return bookDAO.insert(new Book(title, author, genre));
    }

    public int updateById(int bookId, String title, int authorId, int genreId){
        Author author = authorDAO.getById(authorId);
        if (author == null){
            return -1;
        }
        Genre genre = genreDAO.getById(genreId);
        if (genre == null){
            return -2;
        }
        if (bookDAO.getById(bookId) == null) {
            return -3;
        }
        return bookDAO.updateById(bookId, new Book(title, author, genre));
    }

    public boolean deleteByTitle(String title){
        if (bookDAO.getByTitle(title).size()>0){
            bookDAO.deleteByTitle(title);
            return true;
        }
        else {
            return false;
        }
    }

    public boolean deleteAll(){
        bookDAO.deleteAll();
        return true;
    }

    public List<String> getByTitle(String title){
        ArrayList<String> books = new ArrayList<>();
        for (Book book: bookDAO.getByTitle(title)){
            books.add(book.toString());
        }
        if (books.size()>0){
            return books;
        }
        else {
            return null;
        }
    }

    public List<String> getByAuthorId(int authorId){
        ArrayList<String> books = new ArrayList<>();
        Author author = authorDAO.getById(authorId);
        if (author == null){
            books.add(String.format("Автор по идентификатору %d не найден", authorId));
            return books;
        }
        for (Book book: bookDAO.getByAuthor(author)){
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
        Genre genre = genreDAO.getByName(genreName);
        if (genre == null){
            books.add(String.format("Жанр %s не найден", genreName));
            return books;
        }

        for (Book book: bookDAO.getByGenre(genre)){
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
