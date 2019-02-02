package ru.otus.library.domain;

import lombok.Getter;
import java.sql.Date;

public class Author {

    @Getter
    private long id;
    @Getter
    private final String firstName;
    @Getter
    private final String secondName;
    @Getter
    private final Date birthday;

    public Author(long id, String firstName, String secondName, Date birthday) {
        this.id = id;
        this.firstName = firstName;
        this.secondName = secondName;
        this.birthday = birthday;
    }

    public Author(String firstName, String secondName, Date birthday) {
        this.firstName = firstName;
        this.secondName = secondName;
        this.birthday = birthday;
    }

    @Override
    public String toString() {
        return "Автор{" +
                "id=" + id +
                ", имя=:'" + firstName + '\'' +
                ", фамилия:'" + secondName + '\'' +
                ", день рождения:" + birthday +
                '}';
    }
}
