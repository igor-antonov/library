package ru.otus.library.shell;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.library.service.ReviewService;

import java.util.ArrayList;
import java.util.List;

@ShellComponent
public class ReviewCommand {

    private final ReviewService reviewService;

    public ReviewCommand(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @ShellMethod(value = "Добавление рецензии", key = "newreview")
    public String add(long bookId, String reviewer, String text){
        long result = reviewService.add(bookId, reviewer, text);
        if (result == -1){
            return String.format("Книга по идентификатору %d не найдена", bookId);
        }
        else {
            return String.format("Добавлен комментарий с идентификатором %d", result);
        }
    }

    @ShellMethod(value = "Изменение комментария по идентификатору", key = "updatereview")
    public String update(long id, long bookId, String reviewer, String text){
        if (reviewService.updateById(id, bookId, reviewer, text)){
            return String.format("Комментарий с идентификатором %d изменен", id);
        }
        else {
            return String.format("Комментарий с идентификатором %d не найден", id);
        }
    }

    @ShellMethod(value = "Поиск комментариев по книге", key = "getreviewsbyb")
    public List<String> getByBookId(int bookId){
        ArrayList<String> reviews = new ArrayList<>();
        if (reviewService.getByBookId(bookId) != null){
            return reviewService.getByBookId(bookId);
        }
        else {
            reviews.add("Результаты не найдены");
            return reviews;
        }
    }

    @ShellMethod(value = "Удаление всех рецензий", key = "deletereviewall")
    public String deleteAll(){
        if (reviewService.deleteAll()){
            return String.format("Таблица рецензий очищена");
        }
        else {
            return "При удалении произошла ошибка";
        }
    }
}
