package ru.otus.library.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.library.domain.Author;
import ru.otus.library.exception.DataNotFoundException;
import ru.otus.library.repository.AuthorRepository;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class AuthorService {
    private final AuthorRepository authorRepository;

    public AuthorService(AuthorRepository authorRepository){
        this.authorRepository = authorRepository;
    }

    public Author add(String firstName, String secondName, LocalDate birthday){
        return authorRepository.save(new Author(firstName, secondName, birthday));
    }

    public Author getByFirstNameAndSecondNameAndBirthday(String firstName, String secondName, LocalDate birthday)
            throws DataNotFoundException {
        List<Author> result = authorRepository.findByFirstNameAndSecondNameAndBirthday(firstName, secondName, birthday);
        if (result.isEmpty()){
            return add(firstName, secondName, birthday);
        }
        else if (result.size() > 1){
            throw new DataNotFoundException("Найдено несколько результатов");
        }
        else {
            return result.get(0);
        }
    }
}