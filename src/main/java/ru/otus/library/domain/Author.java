package ru.otus.library.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.sql.Date;

@Data
@NoArgsConstructor
@Entity
@Table(name = "authors")
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "second_name")
    private String secondName;
    private Date birthday;

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
