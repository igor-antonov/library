package ru.otus.library.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@Document(collection = "reviews")
public class Review {

    @Id
    private String id;

    private Book book;
    private String text;
    private String reviewer;

    @Field("creation_date")
    private LocalDate creationDate;

    public Review(Book book, String reviewer, String text) {
        this.book = book;
        this.text = text;
        this.reviewer = reviewer;
        this.creationDate = LocalDate.now();
    }
}
