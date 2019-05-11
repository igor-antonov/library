package ru.otus.library.config;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;
import ru.otus.library.domain.Author;
import ru.otus.library.domain.BookMongo;
import ru.otus.library.domain.Genre;

import javax.annotation.PostConstruct;
import java.time.LocalDate;

@Component
public class MongoMigration {

    private final MongoTemplate mongoTemplate;

    public MongoMigration(MongoTemplate mongoTemplate){
        this.mongoTemplate = mongoTemplate;
    }

    @PostConstruct
    private void init(){
        mongoTemplate.save(new BookMongo("Тобол. Мало избранных"
                , new Author("Алексей", "Иванов", LocalDate.of(1969, 11,23))
                , new Genre("Роман")));
        mongoTemplate.save(new BookMongo("Люди среди деревьев"
                , new Author("Ханья", "Янагихара", LocalDate.of(1974, 9,20))
                , new Genre("Роман")));
        mongoTemplate.save(new BookMongo("Homo Deus. Краткая история будущего"
                , new Author("Юваль", "Харари", LocalDate.of(1976, 2,24))
                , new Genre("Эссе")));
    }
}
