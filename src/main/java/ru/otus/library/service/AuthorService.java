package ru.otus.library.service;

import org.springframework.stereotype.Service;
import ru.otus.library.dao.AuthorDAO;
import ru.otus.library.domain.Author;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Service
public class AuthorService {
    private final AuthorDAO authorDAO;

    public AuthorService(AuthorDAO authorDAO){
        this.authorDAO = authorDAO;
    }

    public int add(String firstName, String secondName, Date birthday){
        return authorDAO.insert(firstName, secondName, birthday);
    }

    public int updateBySecondName(String oldSecondName, String firstName, String secondName, Date birthday){
        return authorDAO.updateBySecondName(oldSecondName, firstName, secondName, birthday);
    }

    public int updateById(int id, String firstName, String secondName, Date birthday){
        if (authorDAO.getById(id) != null) {
            return authorDAO.updateById(id, firstName, secondName, birthday);
        }
        else {
            return -1;
        }
    }

    public boolean deleteById(int id){
        if (authorDAO.getById(id) != null){
            authorDAO.deleteById(id);
            return true;
        }
        else {
            return false;
        }
    }

    public boolean deleteAll(){
        authorDAO.deleteAll();
        return true;
    }

    public List<String> getByFirstNameAndSecondName(String firstName, String secondName){
        ArrayList<String> authors = new ArrayList<>();
        for (Author author: authorDAO.getByFirstNameAndSecondName(firstName, secondName)){
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
        for (Author author: authorDAO.getBySecondName(secondName)){
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
        for (Author author: authorDAO.getByBirthday(birthday)){
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
        for (Author author: authorDAO.getAll()){
            authors.add(author.toString());
        }
        if (authors.size()>0){
            return authors;
        }
        else {
            return null;
        }
    }

    public int getCount(){
        return authorDAO.count();
    }
}
