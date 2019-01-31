package ru.otus.library.shell;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.library.dao.BookDAO;
import ru.otus.library.domain.Author;
import ru.otus.library.domain.Book;
import ru.otus.library.domain.Genre;
import ru.otus.library.service.BookService;
import java.util.ArrayList;
import java.util.List;

@ShellComponent
public class BookCommand {

    private final BookDAO bookDAO;
    private final BookService bookService;

    public BookCommand(BookDAO bookDAO, BookService bookService) {
        this.bookDAO = bookDAO;
        this.bookService = bookService;
    }

    @ShellMethod(value = "Добавление книги", key = "newbook")
    public String add(String title, int authorId, int genreId){
        Author author = bookService.getAuthor(authorId);
        if (author == null){
            return String.format("Автор по идентификатору %d не найден", authorId);
        }
        Genre genre = bookService.getGenre(genreId);
        if (genre == null){
            return String.format("Жанр по идентификатору %d не найден", genreId);
        }
        return String.format("Добавлена книга %s с идентификатором %d",
                title, bookDAO.insert(new Book(title, author, genre)));
    }

    @ShellMethod(value = "Изменение книги", key = "editbook")
    public String updateByTitle(int bookId, String title, int authorId, int genreId){
        Author author = bookService.getAuthor(authorId);
        if (author == null){
            return String.format("Автор по идентификатору %d не найден", authorId);
        }
        Genre genre = bookService.getGenre(genreId);
        if (genre == null){
            return String.format("Жанр по идентификатору %d не найден", genreId);
        }
        if (bookDAO.getById(bookId) != null){
            bookDAO.updateById(bookId, new Book(title, author, genre));
            return String.format("Книга с идентификатором %d изменена", bookId);
        }
        else {
            return String.format("Книга с идентификатором %d не найдена", bookId);
        }
    }

    @ShellMethod(value = "Удаление книги по названию", key = "deletebook")
    public String deleteByTitle(String title){
        if (bookDAO.getByTitle(title).size()>0){
            bookDAO.deleteByTitle(title);
            return String.format("Книга %s удалена", title);
        }
        else {
            return String.format("Книга %s не найдена", title);
        }
    }

    @ShellMethod(value = "Удаление всех книг", key = "deletebookall")
    public String deleteAll(){
        bookDAO.deleteAll();
        return String.format("Таблица книг очищена");
    }

    @ShellMethod(value = "Поиск книг по названию", key = "getbooksbyt")
    public List<String> getByTitle(String title){
        ArrayList<String> books = new ArrayList<>();
        for (Book book: bookDAO.getByTitle(title)){
            books.add(book.toString());
        }
        if (books.size()>1){
            return books;
        }
        else {
            books.add("Результаты не найдены");
            return books;
        }
    }

    @ShellMethod(value = "Поиск книг по автору", key = "getbooksbya")
    public List<String> getByAuthorId(int authorId){
        ArrayList<String> books = new ArrayList<>();
        Author author = bookService.getAuthor(authorId);
        if (author == null){
            books.add(String.format("Автор по идентификатору %d не найден", authorId));
            return books;
        }

        for (Book book: bookDAO.getByAuthor(bookService.getAuthor(authorId))){
            books.add(book.toString());
        }
        if (books.size()>1){
            return books;
        }
        else {
            books.add("Результаты не найдены");
            return books;
        }
    }
    @ShellMethod(value = "Поиск книг по жанру", key = "getbooksbyg")
    public List<String> getByGenreName(String genreName){
        ArrayList<String> books = new ArrayList<>();
        Genre genre = bookService.getGenre(genreName);

        if (genre == null){
            books.add(String.format("Жанр %s не найден", genreName));
            return books;
        }

        for (Book book: bookDAO.getByGenre(genre)){
            books.add(book.toString());
        }
        if (books.size()>1){
            return books;
        }
        else {
            books.add("Результаты не найдены");
            return books;
        }
    }
}
