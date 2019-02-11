package ru.otus.library.service;

import org.springframework.stereotype.Service;
import ru.otus.library.repository.GenreRepository;
import ru.otus.library.domain.Genre;

import javax.persistence.NoResultException;
import java.util.ArrayList;
import java.util.List;

@Service
public class GenreService {

    private final GenreRepository genreRepository;

    public GenreService(GenreRepository genreRepository){
        this.genreRepository = genreRepository;
    }

    public long add(String genreName) {
        try {
            genreRepository.getByName(genreName);
            return -1;
        }
        catch (NoResultException e){
            return genreRepository.insert(new Genre(genreName));
        }
    }

    public boolean update(String oldName, String newName){
        try {
            genreRepository.updateByName(oldName, newName);
            return true;
        }
        catch (NoResultException e){
            return false;
        }
    }

    public boolean delete(String name){
        try {
            genreRepository.deleteByName(name);
            return true;
        }
        catch (NoResultException e){
            return false;
        }
    }

    public boolean deleteAll(){
        genreRepository.deleteAll();
        return true;
    }

    public List<String> getAll(){
        ArrayList<String> genres = new ArrayList<>();
        for (Genre genre: genreRepository.getAll()){
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
            return genreRepository.getByName(name).toString();
        }
        catch (NoResultException e){
            return String.format("Жанр %s не найден", name);
        }
    }

    public long getCount(){
        return genreRepository.count();
    }
}
