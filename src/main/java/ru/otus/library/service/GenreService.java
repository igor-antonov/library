package ru.otus.library.service;

import org.springframework.stereotype.Service;
import ru.otus.library.dao.GenreDAO;
import ru.otus.library.domain.Genre;
import java.util.ArrayList;
import java.util.List;

@Service
public class GenreService {

    private final GenreDAO genreDAO;

    public GenreService(GenreDAO genreDAO){
        this.genreDAO = genreDAO;
    }

    public int add(String genreName) {
        if (genreDAO.getByName(genreName) != null){
            return -1;
        }
        else {
            return genreDAO.insert(new Genre(genreName));
        }
    }

    public boolean update(String oldName, String newName){
        if (genreDAO.getByName(oldName) != null) {
            genreDAO.updateByName(oldName, newName);
            return true;
        }
        else {
            return false;
        }
    }

    public boolean delete(String name){
        if (genreDAO.getByName(name) != null) {
            genreDAO.deleteByName(name);
            return true;
        }
        else {
            return false;
        }
    }

    public boolean deleteAll(){
        genreDAO.deleteAll();
        return true;
    }

    public List<String> getAll(){
        ArrayList<String> genres = new ArrayList<>();
        for (Genre genre: genreDAO.getAll()){
            genres.add(genre.toString());
        }
        if (genres.size()>0){
            return genres;
        }
        else {
            return null;
        }
    }

    public String getByName(String name){
        try {
            return genreDAO.getByName(name).toString();
        }
        catch (NullPointerException e){
            return String.format("Жанр %s не найден", name);
        }
    }

    public int getCount(){
        return genreDAO.count();
    }
}
