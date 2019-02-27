package ru.otus.library.shell;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.library.exception.DataNotFoundException;
import ru.otus.library.service.ReviewService;

import java.util.Collections;
import java.util.List;

@ShellComponent
public class ReviewCommand {

    private final ReviewService reviewService;

    public ReviewCommand(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @ShellMethod(value = "Добавление рецензии", key = "newreview")
    public String add(String bookId, String reviewer, String text) {
        try {
            long result = reviewService.add(Long.valueOf(bookId), reviewer, text);
            if (result == -1) {
                return String.format("Книга по идентификатору %s не найдена", bookId);
            } else {
                return String.format("Добавлен комментарий с идентификатором %d", result);
            }
        }
        catch (IllegalArgumentException ex){
            return "Идентификаторы должны быть в числовом формате";
        }
    }

    @ShellMethod(value = "Изменение комментария по идентификатору", key = "updatereview")
    public String update(String id, String bookId, String reviewer, String text){
        try {
            if (reviewService.updateById(Long.valueOf(id), Long.valueOf(bookId), reviewer, text)) {
                return String.format("Комментарий с идентификатором %s изменен", id);
            } else {
                return String.format("Комментарий с идентификатором %s не найден", id);
            }
        }
        catch (IllegalArgumentException ex){
            return "Идентификаторы должны быть в числовом формате " + ex.getMessage();
        }
    }

    @ShellMethod(value = "Поиск комментариев по книге", key = "getreviewsbyb")
    public List<String> getByBookId(String bookId){
        try {
            return reviewService.getByBookId(Long.valueOf(bookId));
        }
        catch (IllegalArgumentException ex){
            return Collections.singletonList("Идентификаторы должны быть в числовом формате");
        }
        catch (DataNotFoundException ex){
            return Collections.singletonList(ex.getMessage());
        }
    }

    @ShellMethod(value = "Поиск комментариев по идентификатору", key = "getreviewsbyid")
    public String getById(String reviewId){
        try {
            return reviewService.getById(Long.valueOf(reviewId));
        }
        catch (IllegalArgumentException ex){
            return "Идентификаторы должны быть в числовом формате";
        }
    }

    @ShellMethod(value = "Удаление всех рецензий", key = "deletereviewall")
    public String deleteAll(){
        if (reviewService.deleteAll()){
            return "Таблица рецензий очищена";
        }
        else {
            return "При удалении произошла ошибка";
        }
    }
}
