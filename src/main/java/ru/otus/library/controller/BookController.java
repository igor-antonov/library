package ru.otus.library.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.otus.library.domain.Author;
import ru.otus.library.domain.Book;
import ru.otus.library.domain.Genre;
import ru.otus.library.exception.DataNotFoundException;
import ru.otus.library.service.BookService;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Controller
public class BookController {
    private final Logger log = LoggerFactory.getLogger(BookController.class);
    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/")
    public String getAll(Model model) {
        List<Book> books = bookService.getAll();
        model.addAttribute("books", books);
        return "all";
    }

    @GetMapping("/delete/{id}")
    public String deleteById(@PathVariable("id") String id, Model model) {
        try {
            bookService.deleteById(id);
            return "redirect:/";
        }
        catch (DataNotFoundException e){
            model.addAttribute("message", e.getMessage());
            log.error(e.getMessage());
            log.error(Arrays.toString(e.getStackTrace()));
            return "error";
        }

    }

    @PostMapping("/add")
    public String add(Model model, @RequestParam("title") String title,
                      @RequestParam("genre") String genreName,
                      @RequestParam("first-name") String firstName,
                      @RequestParam("second-name") String secondName,
                      @RequestParam("birthday") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate birthday) {

        Book book = new Book(title, new Author(firstName, secondName, birthday), new Genre(genreName));
        bookService.add(book);
        List<Book> books = bookService.getAll();
        model.addAttribute("books", books);
        model.addAttribute(books);
        return "redirect:/";
    }

    @GetMapping("/edit")
    public String edit(@RequestParam("id") String id, Model model){
        try {
            Book book = bookService.getById(id);
            model.addAttribute("book", book);
            return "edit";
        }
        catch (DataNotFoundException e){
            model.addAttribute("message", e.getMessage());
            log.error(e.getMessage());
            log.error(Arrays.toString(e.getStackTrace()));
            return "error";
        }
    }

    @PostMapping("/edit")
    public String edit(Model model,@RequestParam("id") String bookId,
                      @RequestParam("title") String title,
                      @RequestParam("genre") String genreName,
                      @RequestParam("first-name") String firstName,
                      @RequestParam("second-name") String secondName,
                      @RequestParam("birthday") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate birthday) {
        try {
            Book book = new Book(title, new Author(firstName, secondName, birthday), new Genre(genreName));
            bookService.updateById(bookId, book);
            return "redirect:/";
        }
        catch (DataNotFoundException e){
            model.addAttribute("message", e.getMessage());
            log.error(e.getMessage());
            log.error(Arrays.toString(e.getStackTrace()));
            return "error";
        }
    }
}
