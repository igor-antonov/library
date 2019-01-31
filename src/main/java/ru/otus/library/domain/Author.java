package ru.otus.library.domain;

import java.sql.Date;

public class Author {

    private long id;
    private final String firstName;
    private final String secondName;
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

    public long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public Date getBirthday() {
        return birthday;
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
