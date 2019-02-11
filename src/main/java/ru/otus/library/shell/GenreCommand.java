package ru.otus.library.shell;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.library.service.GenreService;

import java.util.ArrayList;
import java.util.List;

@ShellComponent
public class GenreCommand {

    private final GenreService genreService;

    public GenreCommand(GenreService genreService){
        this.genreService = genreService;
    }

    @ShellMethod(value = "Добавление жанра", key = "newgenre")
    public String add(String genreName) {
        long result = genreService.add(genreName);
        if (result == -1){
            return String.format("Жанр %s уже существует" , genreName);
        }
        else {
            return String.format("Добавлен жанр %s с идентификатором %s",
                    genreName, result);
        }
    }

    @ShellMethod(value = "Изменение жанра по имени", key = "updategenre")
    public String update(String oldName, String newName){
        if (genreService.update(oldName, newName)){
            return String.format("Жанр %s изменен на %s", oldName, newName);
        }
        else {
            return String.format("Жанр %s не найден", oldName);
        }
    }

    @ShellMethod(value = "Удаление жанра по имени", key = "deletegenre")
    public String delete(String name){
        if (genreService.delete(name)){
            return String.format("Жанр %s удален", name);
        }
        else {
            return String.format("Жанр %s не найден", name);
        }
    }

    @ShellMethod(value = "удаление всех жанров", key = "deletegenreall")
    public String deleteAll(){
        if (genreService.deleteAll()) {
            return "таблица авторов очищена";
        }
        else {
            return "при удалении произошла ошибка";
        }
    }

    @ShellMethod(value = "Поиск всех жанров", key = "getallgenres")
    public List<String> getAll(){
        List<String> genres = new ArrayList<>();
        if (genreService.getAll() != null){
            return genreService.getAll();
        }
        else {
            genres.add("Результаты не найдены");
            return genres;
        }
    }

    @ShellMethod(value = "Поиск жанра по имени", key = "getgenresbyname")
    public String getByName(String name){
        return genreService.getByName(name);
    }

    @ShellMethod(value = "Показать количество жанров", key = "getcountgenres")
    public String getCount(){
        return "Количество жанров: " + genreService.getCount();
    }
}
