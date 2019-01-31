package ru.otus.library.shell;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.library.dao.GenreDAO;
import ru.otus.library.domain.Genre;
import java.util.ArrayList;
import java.util.List;

@ShellComponent
public class GenreCommand {

    private final GenreDAO genreDAO;

    public GenreCommand(GenreDAO genreDAO){
        this.genreDAO = genreDAO;
    }

    @ShellMethod(value = "Добавление жанра", key = "newgenre")
    public String add(String genreName) {
        if (genreDAO.getByName(genreName) != null){
            return String.format("Жанр %s уже существует" , genreName);
        }
        else {
            return String.format("Добавлен жанр %s с идентификатором %s",
                    genreName, genreDAO.insert(new Genre(genreName)));
        }
    }

    @ShellMethod(value = "Изменение жанра по имени", key = "updategenre")
    public String update(String oldName, String newName){
        if (genreDAO.getByName(oldName) != null) {
            genreDAO.updateByName(oldName, newName);
            return String.format("Жанр %s изменен на %s", oldName, newName);
        }
        else {
            return String.format("Жанр %s не найден", oldName);
        }
    }

    @ShellMethod(value = "Удаление жанра по имени", key = "deletegenre")
    public String delete(String name){
        if (genreDAO.getByName(name) != null) {
            genreDAO.deleteByName(name);
            return String.format("Жанр %s удален", name);
        }
        else {
            return String.format("Жанр %s не найден", name);
        }
    }

    @ShellMethod(value = "удаление всех жанров", key = "deletegenreall")
    public String deleteAll(){
        genreDAO.deleteAll();
        return String.format("таблица авторов очищена");
    }

    @ShellMethod(value = "Поиск всех жанров", key = "getallgenres")
    public List<String> getAll(){
        ArrayList<String> genres = new ArrayList<>();
        for (Genre genre: genreDAO.getAll()){
            genres.add(genre.toString());
        }
        if (genres.size()>1){
            return genres;
        }
        else {
            genres.add("Результаты не найдены");
            return genres;
        }
    }

    @ShellMethod(value = "Поиск жанра по имени", key = "getgenresbyname")
    public String getByName(String name){
        try {
            return genreDAO.getByName(name).toString();
        }
        catch (NullPointerException e){
            return String.format("Жанр %s не найден", name);
        }
    }

    @ShellMethod(value = "Показать количество жанров", key = "getcountgenres")
    public String getCount(){
        return "Количество жанров: " + genreDAO.count();
    }
}
