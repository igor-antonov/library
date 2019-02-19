package ru.otus.library.service;

import org.springframework.stereotype.Service;
import ru.otus.library.exception.DataNotFoundException;
import ru.otus.library.repository.AuthorRepository;
import ru.otus.library.domain.Author;

import javax.persistence.NoResultException;
import java.sql.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthorService {
    private final AuthorRepository authorRepository;

    public AuthorService(AuthorRepository authorRepository){
        this.authorRepository = authorRepository;
    }

    public long add(String firstName, String secondName, Date birthday){
        return authorRepository.insert(new Author(firstName, secondName, birthday));
    }

    public boolean updateBySecondName(String oldSecondName, String firstName, String secondName, Date birthday)
            throws DataNotFoundException {
        return authorRepository.updateBySecondName(oldSecondName, firstName, secondName, birthday);
    }

    public boolean updateById(int id, String firstName, String secondName, Date birthday){
        return authorRepository.updateById(id, firstName, secondName, birthday);
    }

    public boolean deleteById(long id){
        try {
            return authorRepository.deleteById(id);
        }
        catch (NoResultException e){
            return false;
        }
    }

    public boolean deleteAll(){
        return authorRepository.deleteAll();
    }

    public List<String> getByFirstNameAndSecondName(String firstName, String secondName)
            throws DataNotFoundException {
        List<String> result = authorRepository.getByFirstNameAndSecondName(firstName, secondName)
                .stream().
                        map(Author::toString)
                .collect(Collectors.toList());
        if (result.isEmpty()){
            throw new DataNotFoundException("results not found");
        }
        else {
            return result;
        }
    }

    public List<String> getBySecondName(String secondName) throws DataNotFoundException{
        List<String> result = authorRepository.getBySecondName(secondName)
                .stream().
                        map(Author::toString)
                .collect(Collectors.toList());
        if (result.isEmpty()){
            throw new DataNotFoundException("results not found");
        }
        else {
            return result;
        }
    }

    public List<String> getByBirthday(Date birthday) throws DataNotFoundException{
        List<String> result = authorRepository.getByBirthday(birthday)
                .stream().
                        map(Author::toString)
                .collect(Collectors.toList());
        if (result.isEmpty()){
            throw new DataNotFoundException("results not found");
        }
        else {
            return result;
        }
    }

    public List<String> getAll() throws DataNotFoundException {
        List<String> result = authorRepository.getAll()
                .stream().
                        map(Author::toString)
                .collect(Collectors.toList());
        if (result.isEmpty()){
            throw new DataNotFoundException("results not found");
        }
        else {
            return result;
        }
    }

    public long getCount(){
        return authorRepository.count();
    }
}
