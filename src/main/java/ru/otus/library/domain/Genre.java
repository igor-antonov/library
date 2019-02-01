package ru.otus.library.domain;

import lombok.Getter;

public class Genre {

    @Getter
    private long id;
    @Getter
    private String name;

    public Genre(long id, String name) {
        this.id = id;
        this.name = name;
    }
    public Genre(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Жанр{" +
                "id=" + id +
                ", название:'" + name + '\'' +
                '}';
    }
}
