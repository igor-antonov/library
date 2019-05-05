package ru.otus.library.rest.dto;

import lombok.Data;
import ru.otus.library.domain.Author;
import ru.otus.library.domain.Book;
import ru.otus.library.domain.Genre;

@Data
public class BookDto {

    private Long id;
    private String title;
    private Genre genre;
    private Author author;

    public BookDto(Long id, String title, Genre genre, Author author){
        this.id = id;
        this.title = title;
        this.genre = genre;
        this.author = author;

    }

    @Override
    public String toString() {
        return "BookDto{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", genre=" + genre +
                ", author=" + author +
                '}';
    }

    public static BookDto toDto(Book book){
        return new BookDto(book.getId(),
                book.getTitle(),
                book.getGenre(),
                book.getAuthor());
    }
}
