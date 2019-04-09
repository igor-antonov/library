package ru.otus.library.rest.dto;

import ru.otus.library.domain.Author;
import ru.otus.library.domain.Book;
import ru.otus.library.domain.Genre;

import java.util.Objects;

public class BookDto {

    private String id;
    private String title;
    private Genre genre;
    private Author author;

    public BookDto(String id, String title, Genre genre, Author author){
        this.id = id;
        this.title = title;
        this.genre = genre;
        this.author = author;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BookDto)) return false;
        BookDto bookDto = (BookDto) o;
        return Objects.equals(id, bookDto.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public static BookDto toDto(Book book){
        return new BookDto(book.getId(),
                book.getTitle(),
                book.getGenre(),
                book.getAuthor());
    }
}
