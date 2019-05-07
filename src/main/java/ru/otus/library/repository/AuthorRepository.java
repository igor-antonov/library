package ru.otus.library.repository;

import org.springframework.data.repository.CrudRepository;
import ru.otus.library.domain.Author;

import java.time.LocalDate;
import java.util.List;

public interface AuthorRepository extends CrudRepository<Author, Long> {
    List<Author> findByFirstNameAndSecondNameAndBirthday(String firstName, String secondName, LocalDate birthday);
    Author save(Author author);
}