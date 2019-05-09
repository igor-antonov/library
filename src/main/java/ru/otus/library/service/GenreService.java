package ru.otus.library.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.library.domain.Genre;
import ru.otus.library.repository.GenreRepository;

import java.util.Optional;

@Service
@Transactional
public class GenreService {

    private final GenreRepository genreRepository;

    public GenreService(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    public Genre add(String genreName) {
        Optional<Genre> genreOptional = genreRepository.findByName(genreName);
        return genreOptional.orElseGet(() -> genreRepository.save(new Genre(genreName)));
    }
    public Genre getByName(String name) {
        Optional<Genre> genres = genreRepository.findByName(name);
        return genres.orElseGet(() -> add(name));
    }
}