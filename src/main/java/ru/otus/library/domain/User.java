package ru.otus.library.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "usr")
public class User {

    @Id
    String id;

    String userName;
    String password;

    public User(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }
}
