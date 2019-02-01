package ru.otus.library.shell;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.library.service.BookService;
import java.util.ArrayList;
import java.util.List;

@ShellComponent
public class BookCommand {

    private final BookService bookService;

    public BookCommand(BookService bookService) {
        this.bookService = bookService;
    }

    @ShellMethod(value = "Добавление книги", key = "newbook")
    public String add(String title, int authorId, int genreId){
        int result = bookService.add(title, authorId, genreId);

        switch (result){
            case -1: return String.format("Автор по идентификатору %d не найден", authorId);
            case -2: return String.format("Жанр по идентификатору %d не найден", genreId);
            default: return String.format("Добавлена книга %s с идентификатором %d", title, result);
        }
    }

    @ShellMethod(value = "Изменение книги", key = "editbook")
    public String updateByTitle(int bookId, String title, int authorId, int genreId){
        int result = bookService.updateById(bookId, title, authorId, genreId);

        switch (result){
            case -1: return String.format("Автор по идентификатору %d не найден", authorId);
            case -2: return String.format("Жанр по идентификатору %d не найден", genreId);
            case -3: return String.format("Книга с идентификатором %d не найдена", bookId);
            default: return String.format("Книга с идентификатором %d изменена", bookId);
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
            return String.format("Таблица книг очищена");
        }
        else {
            return "при удалении произошла ошибка";
        }
    }

    @ShellMethod(value = "Поиск книг по названию", key = "getbooksbyt")
    public List<String> getByTitle(String title){
        List<String> books = new ArrayList<>();
        if (bookService.getByTitle(title) != null){
            return bookService.getByTitle(title);
        }
        else {
            books.add("Результаты не найдены");
            return books;
        }
    }

    @ShellMethod(value = "Поиск книг по автору", key = "getbooksbya")
    public List<String> getByAuthorId(int authorId){
        ArrayList<String> books = new ArrayList<>();
        if (bookService.getByAuthorId(authorId) != null){
            return bookService.getByAuthorId(authorId);
        }
        else {
            books.add("Результаты не найдены");
            return books;
        }
    }
    @ShellMethod(value = "Поиск книг по жанру", key = "getbooksbyg")
    public List<String> getByGenreName(String genreName){
        ArrayList<String> books = new ArrayList<>();
        if (bookService.getByGenreName(genreName) != null){
            return bookService.getByGenreName(genreName);
        }
        else {
            books.add("Результаты не найдены");
            return books;
        }
    }
}
