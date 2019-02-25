package ru.otus.library.service;

import org.springframework.stereotype.Service;
import ru.otus.library.exception.DataNotFoundException;
import ru.otus.library.repository.GenreRepository;
import ru.otus.library.domain.Genre;

import javax.persistence.NoResultException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GenreService {

    private final GenreRepository genreRepository;

    public GenreService(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    public long add(String genreName) {
        try {
            genreRepository.getByName(genreName);
            return -1;
        } catch (NoResultException e) {
            return genreRepository.insert(new Genre(genreName));
        }
    }

    public boolean update(String oldName, String newName) {
        try {
            return genreRepository.updateByName(oldName, newName);
        } catch (NoResultException e) {
            return false;
        }
    }

    public boolean delete(String name) {
        try {
            return genreRepository.deleteByName(name);
        } catch (NoResultException e) {
            return false;
        }
    }

    public boolean deleteAll() {
        return genreRepository.deleteAll();
    }

    public List<String> getAll() throws DataNotFoundException {
        List<String> result = genreRepository.getAll()
                .stream().
                        map(Genre::toString)
                .collect(Collectors.toList());
        if (result.isEmpty()) {
            throw new DataNotFoundException("results not found");
        } else {
            return result;
        }
    }

    public String getByName(String name) {
        try {
            return genreRepository.getByName(name).toString();
        } catch (NullPointerException e) {
            return String.format("Жанр %s не найден", name);
        }
    }

    public long getCount(){
        return genreRepository.count();
    }
}
