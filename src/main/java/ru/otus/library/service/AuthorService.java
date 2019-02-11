package ru.otus.library.service;

import org.springframework.stereotype.Service;
import ru.otus.library.repository.AuthorRepository;
import ru.otus.library.domain.Author;

import javax.persistence.NoResultException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Service
public class AuthorService {
    private final AuthorRepository authorRepository;

    public AuthorService(AuthorRepository authorRepository){
        this.authorRepository = authorRepository;
    }

    public long add(String firstName, String secondName, Date birthday){
        return authorRepository.insert(new Author(firstName, secondName, birthday));
    }

    public boolean updateBySecondName(String oldSecondName, String firstName, String secondName, Date birthday){
        if (authorRepository.updateBySecondName(oldSecondName, firstName, secondName, birthday)){
            return true;
        }
        else {
            return false;
        }
    }

    public boolean updateById(int id, String firstName, String secondName, Date birthday){
        if (authorRepository.updateById(id, firstName, secondName, birthday)) {
            return true;
        }
        else {
            return false;
        }
    }

    public boolean deleteById(long id){
        try {
            authorRepository.deleteById(id);
            return true;
        }
        catch (NoResultException e){
            return false;
        }
    }

    public boolean deleteAll(){
        authorRepository.deleteAll();
        return true;
    }

    public List<String> getByFirstNameAndSecondName(String firstName, String secondName){
        ArrayList<String> authors = new ArrayList<>();
        for (Author author: authorRepository.getByFirstNameAndSecondName(firstName, secondName)){
            authors.add(author.toString());
        }
        if (authors.size()>0){
            return authors;
        }
        else {
            return null;
        }

    }

    public List<String> getBySecondName(String secondName){
        ArrayList<String> authors = new ArrayList<>();
        for (Author author: authorRepository.getBySecondName(secondName)){
            authors.add(author.toString());
        }
        if (authors.size()>0){
            return authors;
        }
        else {
            return null;
        }
    }

    public ArrayList<String> getByBirthday(Date birthday){
        ArrayList<String> authors = new ArrayList<>();
        for (Author author: authorRepository.getByBirthday(birthday)){
            authors.add(author.toString());
        }
        if (authors.size()>1){
            return authors;
        }
        else {
            return null;
        }
    }

    public List<String> getAll(){
        ArrayList<String> authors = new ArrayList<>();
        for (Author author: authorRepository.getAll()){
            authors.add(author.toString());
        }
        if (authors.size()>0){
            return authors;
        }
        else {
            return null;
        }
    }

    public long getCount(){
        return authorRepository.count();
    }
}
