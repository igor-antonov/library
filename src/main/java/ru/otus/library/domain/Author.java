package ru.otus.library.domain;

import lombok.Data;

import java.time.LocalDate;

@Data
public class Author {

    private String firstName;
    private String secondName;
    private LocalDate birthday;

    public Author(String firstName, String secondName, LocalDate birthday) {
        this.firstName = firstName;
        this.secondName = secondName;
        this.birthday = birthday;
    }

    @Override
    public String toString() {
        return "Автор{" +
                "имя=:'" + firstName + '\'' +
                ", фамилия:'" + secondName + '\'' +
                ", день рождения:" + birthday +
                '}';
    }
}
