package ru.otus.library.shell;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.library.dao.AuthorDAO;
import ru.otus.library.domain.Author;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@ShellComponent
public class AuthorCommand {

    private final AuthorDAO authorDAO;

    public AuthorCommand(AuthorDAO authorDAO){
        this.authorDAO = authorDAO;
    }

    @ShellMethod(value = "Добавление автора", key = "newauthor")
    public String add(String firstName, String secondName, String birthday){
        try {
            return String.format("Добавлен автор %s %s с идентификатором %s",
                    firstName, secondName, authorDAO.insert(firstName, secondName, Date.valueOf(birthday)));
        }
        catch (IllegalArgumentException e){
            return "Укажите дату рождения в формате YYYY-MM-DD";
        }
    }

    @ShellMethod(value = "Изменение автора", key = "editauthor")
    public String updateBySecondName(String oldSecondName, String firstName, String secondName, String birthday){
        try {
            if (authorDAO.getBySecondName(secondName).size()>0){
                authorDAO.updateBySecondName(oldSecondName, firstName, secondName, Date.valueOf(birthday));
                return String.format("Автор с фамилией %s изменен", oldSecondName);
            }
            else {
                return String.format("Автор с фамилией %s не найден", secondName);
            }
        }
        catch (IllegalArgumentException e){
            return "Укажите дату рождения в формате YYYY-MM-DD";
        }
    }

    @ShellMethod(value = "Удаление автора по фамилии", key = "deleteauthor")
    public String deleteBySecondName(String secondName){
        if (authorDAO.getBySecondName(secondName).size()>0){
            authorDAO.deleteBySecondName(secondName);
            return String.format("Автор с фамилией %s удален", secondName);
        }
        else {
            return String.format("Автор с фамилией %s не найден", secondName);
        }
    }

    @ShellMethod(value = "Удаление всех авторов", key = "deleteauthorall")
    public String deleteAll(){
        authorDAO.deleteAll();
        return "таблица авторов очищена";
    }

    @ShellMethod(value = "Поиск автора по имени и фамилии", key = "getauthorsfs")
    public List<String> getByFirstNameAndSecondName(String firstName, String secondName){
        ArrayList<String> authors = new ArrayList<>();
        for (Author author: authorDAO.getByFirstNameAndSecondName(firstName, secondName)){
            authors.add(author.toString());
        }
        if (authors.size()>1){
            return authors;
        }
        else {
            authors.add("Результаты не найдены");
            return authors;
        }
    }

    @ShellMethod(value = "Поиск автора по фамилии", key = "getauthorss")
    public List<String> getBySecondName(String secondName){
        ArrayList<String> authors = new ArrayList<>();
        for (Author author: authorDAO.getBySecondName(secondName)){
            authors.add(author.toString());
        }
        if (authors.size()>1){
            return authors;
        }
        else {
            authors.add("Результаты не найдены");
            return authors;
        }
    }

    @ShellMethod(value = "Поиск автора по дате рождения", key = "getauthorsb")
    public ArrayList<String> getByBirthday(String birthday){
        ArrayList<String> authors = new ArrayList<>();
        try {
            for (Author author: authorDAO.getByBirthday(Date.valueOf(birthday))){
                authors.add(author.toString());
            }
            if (authors.size()>1){
                return authors;
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
        ArrayList<String> authors = new ArrayList<>();
        for (Author author: authorDAO.getAll()){
            authors.add(author.toString());
        }
        if (authors.size()>1){
            return authors;
        }
        else {
            authors.add("Результаты не найдены");
            return authors;
        }
    }

    @ShellMethod(value = "Показать количество авторов", key = "getauthorsc")
    public String getCount(){
        return "Количество жанров: " + authorDAO.count();
    }

}
