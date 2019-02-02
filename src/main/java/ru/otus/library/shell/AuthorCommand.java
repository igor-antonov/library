package ru.otus.library.shell;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.library.service.AuthorService;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@ShellComponent
public class AuthorCommand {

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
            int result = authorService.updateBySecondName(oldSecondName, firstName, secondName, Date.valueOf(birthday));
            if (result >0){
                return String.format("Автор с фамилией %s изменен", oldSecondName);
            }
            else if (result == -1){
                return "";
            }
            else {
                return String.format("Автор с фамилией %s не найден", secondName);
            }
        }
        catch (IllegalArgumentException e){
            return "Укажите дату рождения в формате YYYY-MM-DD";
        }
    }

    @ShellMethod(value = "Изменение автора", key = "editauthorid")
    public String updateById(String id, String firstName, String secondName, String birthday){
        try {
            int result = authorService.updateById(Integer.valueOf(id), firstName, secondName, Date.valueOf(birthday));
            if (result != -1){
                return String.format("Автор с идентификатором %s изменен", result);
            }
            else {
                return String.format("Автор с фамилией %s не найден", secondName);
            }
        }
        catch (IllegalArgumentException e){
            return "Укажите дату рождения в формате YYYY-MM-DD";
        }
    }

    @ShellMethod(value = "Удаление автора по идентификатору", key = "deleteauthor")
    public String deleteById(int id){
        if (authorService.deleteById(id)){
            return String.format("Автор с идентификатором %d удален", id);
        }
        else {
            return String.format("Автор с идентификатором %d не найден", id);
        }
    }

    @ShellMethod(value = "Удаление всех авторов", key = "deleteauthorall")
    public String deleteAll(){
        if (authorService.deleteAll()){
            return "таблица авторов очищена";
        }
        return "при удалении произошла ошибка";
    }

    @ShellMethod(value = "Поиск автора по имени и фамилии", key = "getauthorsfs")
    public List<String> getByFirstNameAndSecondName(String firstName, String secondName){
        List<String> authors = new ArrayList<>();
        if (authorService.getByFirstNameAndSecondName(firstName, secondName) != null){
            return authorService.getByFirstNameAndSecondName(firstName, secondName);
        }
        else {
            authors.add("Результаты не найдены");
            return authors;
        }
    }

    @ShellMethod(value = "Поиск автора по фамилии", key = "getauthorss")
    public List<String> getBySecondName(String secondName){
        List<String> authors = new ArrayList<>();
        if (authorService.getBySecondName(secondName) != null){
            return authorService.getBySecondName(secondName);
        }
        else {
            authors.add("Результаты не найдены");
            return authors;
        }
    }

    @ShellMethod(value = "Поиск автора по дате рождения", key = "getauthorsb")
    public List<String> getByBirthday(String birthday){
        List<String> authors = new ArrayList<>();
        try{
            if (authorService.getByBirthday(Date.valueOf(birthday)) != null){
                return authorService.getByBirthday(Date.valueOf(birthday));
            }
            else {
                authors.add("Результаты не найдены");
                return authors;
            }
        }
        catch (IllegalArgumentException e){
            authors.add("Укажите дату рождения в формате YYYY-MM-DD");
            return authors;
        }
    }

    @ShellMethod(value = "Найти всех авторов", key = "getallauthors")
    public List<String> getAll(){
        List<String> authors = new ArrayList<>();
        if (authorService.getAll() != null){
            return authorService.getAll();
        }
        else {
            authors.add("Результаты не найдены");
            return authors;
        }
    }

    @ShellMethod(value = "Показать количество авторов", key = "getauthorsc")
    public String getCount(){
        return "Количество жанров: " + authorService.getCount();
    }

}
