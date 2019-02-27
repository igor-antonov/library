package ru.otus.library.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Date;

@Data
@NoArgsConstructor
@Entity
@Table(name = "reviews")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;
    private String text;
    private String reviewer;

    @Column(name = "creation_date")
    private Date creationDate;

    public Review(Book book, String reviewer, String text) {
        this.book = book;
        this.text = text;
        this.reviewer = reviewer;
        this.creationDate = new Date(System.currentTimeMillis());
    }
}
