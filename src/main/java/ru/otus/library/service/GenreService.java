package ru.otus.library.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.library.exception.DataNotFoundException;
import ru.otus.library.repository.GenreRepository;
import ru.otus.library.domain.Genre;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class GenreService {

    private final GenreRepository genreRepository;

    public GenreService(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    public long add(String genreName) {
        if (genreRepository.findByName(genreName).isPresent()){
            return -1;
        }
        else {
            return genreRepository.save(new Genre(genreName)).getId();
        }
    }

    public boolean update(String oldName, String newName) {
        return genreRepository.updateByName(oldName, newName)>0;
    }

    public boolean delete(String name) {
        return genreRepository.deleteByName(name)>0;
    }

    public boolean deleteAll() {
        genreRepository.deleteAll();
        return true;
    }

    public List<String> getAll() throws DataNotFoundException {
        List<String> result = genreRepository.findAll()
                .stream().
                        map(Genre::toString)
                .collect(Collectors.toList());
        if (result.isEmpty()) {
            throw new DataNotFoundException("Результаты не найдены");
        } else {
            return result;
        }
    }

    public String getByName(String name) {
        Optional<Genre> genres = genreRepository.findByName(name);
        if (genres.isPresent()){
            return genres.get().toString();
        }
        else {
            return String.format("Жанр %s не найден", name);
        }
    }

    public long getCount(){
        return genreRepository.count();
    }
}
