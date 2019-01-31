package ru.otus.library.domain;

public class Book {

    private long id;
    private final String title;
    private final Author author;
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

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public Author getAuthor() {
        return author;
    }

    public Genre getGenre() {
        return genre;
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
