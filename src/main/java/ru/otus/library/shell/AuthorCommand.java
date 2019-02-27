package ru.otus.library.shell;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.library.exception.DataNotFoundException;
import ru.otus.library.service.AuthorService;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@ShellComponent
public class AuthorCommand {

    @Autowired
    private final AuthorService authorService;

    public AuthorCommand(AuthorService authorService){
        this.authorService = authorService;
    }

    @ShellMethod(value = "Добавление автора", key = "newauthor")
    public String add(String firstName, String secondName, String birthday){
        try {
            return String.format("Добавлен автор %s %s с идентификатором %s",
                    firstName, secondName, authorService.add(firstName, secondName, Date.valueOf(birthday)));
        }
        catch (IllegalArgumentException e){
            return "Укажите дату рождения в формате YYYY-MM-DD";
        }
    }

    @ShellMethod(value = "Изменение автора", key = "editauthor")
    public String updateBySecondName(String oldSecondName, String firstName, String secondName, String birthday){
        try {
            authorService.updateBySecondName(oldSecondName, firstName, secondName, Date.valueOf(birthday));
            return String.format("Автор с фамилией %s изменен", oldSecondName);
        }
        catch (IllegalArgumentException e){
            return "Укажите дату рождения в формате YYYY-MM-DD";
        }
    }

    @ShellMethod(value = "Изменение автора", key = "editauthorid")
    public String updateById(String id, String firstName, String secondName, String birthday){
        try {
            if (authorService.updateById(Integer.valueOf(id), firstName, secondName, Date.valueOf(birthday))){
                return String.format("Автор с идентификатором %s изменен", id);
            }
            else {
                return String.format("Автор с идентификатором %s не найден", id);
            }
        }
        catch (IllegalArgumentException e){
            return "Укажите дату рождения в формате YYYY-MM-DD";
        }
    }

    @ShellMethod(value = "Удаление автора по идентификатору", key = "deleteauthor")
    public String deleteById(String id){
        try{
            if (authorService.deleteById(Integer.valueOf(id))){
                return String.format("Автор с идентификатором %s удален", id);
            }
            else {
                return String.format("Автор с идентификатором %s не найден", id);
            }
        }
        catch (IllegalArgumentException ex){
            return "идентификаторы должны быть в числовом формате";
        }
    }

    @ShellMethod(value = "Удаление всех авторов", key = "deleteauthorall")
    public String deleteAll(){
        if (authorService.deleteAll()){
            return "таблица авторов очищена";
        }
        else {
            return "при удалении произошла ошибка";
        }
    }

    @ShellMethod(value = "Поиск автора по имени и фамилии", key = "getauthorsfs")
    public List<String> getByFirstNameAndSecondName(String firstName, String secondName){
        try {
            return authorService.getByFirstNameAndSecondName(firstName, secondName);
        }
        catch (DataNotFoundException ex){
            return Collections.singletonList(ex.getMessage());
        }
    }

    @ShellMethod(value = "Поиск автора по фамилии", key = "getauthorss")
    public List<String> getBySecondName(String secondName){
        try {
            return authorService.getBySecondName(secondName);
        }
        catch (DataNotFoundException ex){
            return Collections.singletonList(ex.getMessage());
        }
    }

    @ShellMethod(value = "Поиск автора по дате рождения", key = "getauthorsb")
    public List<String> getByBirthday(String birthday){
        List<String> authors = new ArrayList<>();
        try{
            return authorService.getByBirthday(Date.valueOf(birthday));
        }
        catch (IllegalArgumentException e){
            authors.add("Укажите дату рождения в формате YYYY-MM-DD");
            return authors;
        }
        catch (DataNotFoundException ex){
            return Collections.singletonList(ex.getMessage());
        }
    }

    @ShellMethod(value = "Найти всех авторов", key = "getallauthors")
    public List<String> getAll(){
        try {
            return authorService.getAll();
        }
        catch (DataNotFoundException ex){
            return Collections.singletonList(ex.getMessage());
        }
    }

    @ShellMethod(value = "Показать количество авторов", key = "getauthorsc")
    public String getCount(){
        return "Количество авторов: " + authorService.getCount();
    }
}
