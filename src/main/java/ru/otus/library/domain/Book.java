package ru.otus.library.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity
@Table(name = "books")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String title;
    @OneToOne(cascade=CascadeType.MERGE)
    @JoinColumn(name = "author_id")
    private Author author;
    @OneToOne(cascade=CascadeType.MERGE)
    @JoinColumn(name = "genre_id")
    private Genre genre;

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
