package ru.otus.library.domain;

import lombok.Getter;

public class Book {

    @Getter
    private long id;
    @Getter
    private final String title;
    @Getter
    private final Author author;
    @Getter
    private final Genre genre;

    public Book(long id, String title, Author author, Genre genre) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.genre = genre;
    }

    public Book(String title, Author author, Genre genre) {
        this.title = title;
        this.author = author;
        this.genre = genre;
    }

    @Override
    public String toString() {
        return "Книга{" +
                "id=" + id +
                ", название:'" + title + '\'' +
                ", автор:" + author +
                ", жанр:" + genre +
                '}';
    }
}
