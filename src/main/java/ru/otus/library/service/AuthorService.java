package ru.otus.library.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.library.exception.DataNotFoundException;
import ru.otus.library.repository.AuthorRepository;
import ru.otus.library.domain.Author;

import java.sql.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class AuthorService {
    private final AuthorRepository authorRepository;

    public AuthorService(AuthorRepository authorRepository){
        this.authorRepository = authorRepository;
    }

    public long add(String firstName, String secondName, Date birthday){
        return authorRepository.save(new Author(firstName, secondName, birthday)).getId();
    }

    public boolean updateBySecondName(String oldSecondName, String firstName, String secondName, Date birthday) {
        return authorRepository.updateBySecondName(oldSecondName, firstName, secondName, birthday) > 0;
    }

    public boolean updateById(int id, String firstName, String secondName, Date birthday){
        return authorRepository.updateById(id, firstName, secondName, birthday)>0;
    }

    public boolean deleteById(long id){
        return authorRepository.deleteById(id)>0;
    }

    public boolean deleteAll(){
        authorRepository.deleteAll();
        return true;
    }

    public List<String> getByFirstNameAndSecondName(String firstName, String secondName)
            throws DataNotFoundException {
        List<String> result = authorRepository.findByFirstNameAndSecondName(firstName, secondName)
                .stream().
                        map(Author::toString)
                .collect(Collectors.toList());
        if (result.isEmpty()){
            throw new DataNotFoundException("Результаты не найдены");
        }
        else {
            return result;
        }
    }

    public List<String> getBySecondName(String secondName) throws DataNotFoundException{
        List<String> result = authorRepository.findBySecondName(secondName)
                .stream().
                        map(Author::toString)
                .collect(Collectors.toList());
        if (result.isEmpty()){
            throw new DataNotFoundException("Результаты не найдены");
        }
        else {
            return result;
        }
    }

    public List<String> getByBirthday(Date birthday) throws DataNotFoundException{
        List<String> result = authorRepository.findByBirthday(birthday)
                .stream().
                        map(Author::toString)
                .collect(Collectors.toList());
        if (result.isEmpty()){
            throw new DataNotFoundException("Результаты не найдены");
        }
        else {
            return result;
        }
    }

    public List<String> getAll() throws DataNotFoundException {
        List<String> result = authorRepository.findAll()
                .stream().
                        map(Author::toString)
                .collect(Collectors.toList());
        if (result.isEmpty()){
            throw new DataNotFoundException("Результаты не найдены");
        }
        else {
            return result;
        }
    }

    public long getCount(){
        return authorRepository.count();
    }
}
