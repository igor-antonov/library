package ru.otus.library.service;

import org.springframework.stereotype.Service;
import ru.otus.library.dao.AuthorDAO;
import ru.otus.library.dao.GenreDAO;
import ru.otus.library.domain.Author;
import ru.otus.library.domain.Genre;

@Service
public class BookService {

    private final GenreDAO genreDAO;
    private final AuthorDAO authorDAO;

    public BookService(GenreDAO genreDAO, AuthorDAO authorDAO){
        this.genreDAO = genreDAO;
        this.authorDAO = authorDAO;
    }

    public Author getAuthor(int id){
        return authorDAO.getById(id);
    }

    public Genre getGenre(int id){
        return genreDAO.getById(id);
    }

    public Genre getGenre(String genreName){
        return genreDAO.getByName(genreName);
    }

}
