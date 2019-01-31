package ru.otus.library.domain;

public class Genre {

    private long id;
    private String name;

    public Genre(long id, String name) {
        this.id = id;
        this.name = name;
    }
    public Genre(String name) {
        this.name = name;
    }


    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Жанр{" +
                "id=" + id +
                ", название:'" + name + '\'' +
                '}';
    }
}
