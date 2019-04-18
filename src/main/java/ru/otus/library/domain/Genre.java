package ru.otus.library.domain;

import lombok.Data;

@Data
public class Genre {

    private String name;

    public Genre(){

    }
    public Genre(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Жанр{" +
                "название='" + name + '\'' +
                '}';
    }
}
