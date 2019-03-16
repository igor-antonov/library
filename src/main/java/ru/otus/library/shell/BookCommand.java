package ru.otus.library.shell;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.library.domain.Author;
import ru.otus.library.domain.Book;
import ru.otus.library.domain.Genre;
import ru.otus.library.exception.DataNotFoundException;
import ru.otus.library.service.BookService;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@ShellComponent
public class BookCommand {

    private final BookService bookService;

    public BookCommand(BookService bookService) {
        this.bookService = bookService;
    }

    @ShellMethod(value = "Добавление книги", key = "newbook")
    public String add(String title, String authorFirstName, String authorSecondName, int authorBirthdayYear,
                      int authorBirthdayMonth, int authorBirthdayDay, String genreName){
        try {
            Book book = new Book(title
                    , new Author(authorFirstName, authorSecondName, LocalDate.of(authorBirthdayYear,
                    authorBirthdayMonth, authorBirthdayDay)), new Genre(genreName));
            bookService.add(book);
            return String.format("Добавлена книга с идентификатором: %s", book.getId());
        }
        catch (IllegalArgumentException ex){
            return "Некорректный формат указаных данных";
        }
    }

    @ShellMethod(value = "Поиск книг по названию", key = "getbooksbyt")
    public List<String> getByTitle(String title) {
        try {
            return bookService.getByTitle(title);
        } catch (DataNotFoundException e) {
            return Collections.singletonList(e.getMessage());
        }
    }

    @ShellMethod(value = "Изменение книги", key = "editbook")
    public String updateById(String bookId, String title, String authorFirstName, String authorSecondName,
                             int authorBirthdayYear, int authorBirthdayMonth, int authorBirthdayDay, String genreName) {
        try {bookService.updateById(bookId, new Book(title
                , new Author(authorFirstName, authorSecondName, LocalDate.of(authorBirthdayYear,
                authorBirthdayMonth, authorBirthdayDay)), new Genre(genreName)));
            return String.format("Книга с идентификатором %s изменена", bookId);
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

    @ShellMethod(value = "Поиск книг по автору", key = "getbooksbya")
    public List<String> getByAuthorId(String authorSecondName){
        try {
            return bookService.getByAuthorSecondName(authorSecondName);
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
