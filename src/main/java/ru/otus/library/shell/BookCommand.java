package ru.otus.library.shell;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.library.exception.DataNotFoundException;
import ru.otus.library.service.BookService;
import java.util.Collections;
import java.util.List;

@ShellComponent
public class BookCommand {

    private final BookService bookService;

    public BookCommand(BookService bookService) {
        this.bookService = bookService;
    }

    @ShellMethod(value = "Добавление книги", key = "newbook")
    public String add(String title, String authorId, String genreId){
        try {
            long result = bookService.add(title, Integer.valueOf(authorId), Integer.valueOf(genreId));
            return String.format("Добавлена книга %s с идентификатором %d", title, result);
        }
        catch (IllegalArgumentException ex){
            return "Идентификаторы должны быть в числовом формате";
        }
        catch (DataNotFoundException e){
            return e.getMessage();
        }

    }

    @ShellMethod(value = "Изменение книги", key = "editbook")
    public String updateById(String bookId, String title, String authorId, String genreId) {
        try {bookService.updateById(Integer.valueOf(bookId), title,
                    Integer.valueOf(authorId), Integer.valueOf(genreId));
            return String.format("Книга с идентификатором %s изменена", bookId);
        }
        catch (IllegalArgumentException ex) {
            return "Идентификаторы должны быть в числовом формате";
        }
        catch (DataNotFoundException e){
            return e.getMessage();
        }

    }

    @ShellMethod(value = "Удаление книги по названию", key = "deletebook")
    public String deleteByTitle(String title){
        if (bookService.deleteByTitle(title)){
            return String.format("Книга %s удалена", title);
        }
        else {
            return String.format("Книга %s не найдена", title);
        }
    }

    @ShellMethod(value = "Удаление всех книг", key = "deletebookall")
    public String deleteAll(){
        if (bookService.deleteAll()){
            return "Таблица книг очищена";
        }
        else {
            return "при удалении произошла ошибка";
        }
    }

    @ShellMethod(value = "Поиск книг по названию", key = "getbooksbyt")
    public List<String> getByTitle(String title){
        try {
            return bookService.getByTitle(title);
        }
        catch (DataNotFoundException ex){
            return Collections.singletonList(ex.getMessage());
        }
    }

    @ShellMethod(value = "Поиск книг по автору", key = "getbooksbya")
    public List<String> getByAuthorId(String authorId){
        try {
            return bookService.getByAuthorId(Integer.valueOf(authorId));
        }
        catch (IllegalArgumentException ex){
            return Collections.singletonList("Идентификаторы должны быть в числовом формате");
        }
        catch (DataNotFoundException ex){
            return Collections.singletonList(ex.getMessage());
        }
    }
    @ShellMethod(value = "Поиск книг по жанру", key = "getbooksbyg")
    public List<String> getByGenreName(String genreName){
        try {
            return bookService.getByGenreName(genreName);
        }
        catch (DataNotFoundException ex){
            return Collections.singletonList(ex.getMessage());
        }
    }
}
