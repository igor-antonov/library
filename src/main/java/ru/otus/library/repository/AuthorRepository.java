package ru.otus.library.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import ru.otus.library.domain.Author;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

public interface AuthorRepository extends CrudRepository<Author, Long> {
    Optional<Author> findById(long id);
    List<Author> findByFirstNameAndSecondName(String firstName, String secondName);
    List<Author> findBySecondName(String secondName);
    List<Author> findByBirthday(Date birthday);
    List<Author> findAll();
    Author save(Author author);
    Integer deleteById(long id);
    void deleteAll();

    @Modifying
    @Query(value = "update Author set firstName =:firstName, secondName =:secondName," +
            " birthday =:birthday where secondName =:oldSecondName")
    int updateBySecondName(@Param("oldSecondName") String oldSecondName, @Param("firstName") String firstName,
                           @Param("secondName") String secondName, @Param("birthday") Date birthday);
    @Modifying
    @Query(value = "update Author set firstName =:firstName, secondName =:secondName," +
            " birthday =:birthday where id =:id")
    int updateById(@Param("id") long id, @Param("firstName") String firstName,
                   @Param("secondName") String secondName, @Param("birthday") Date birthday);
    long count();
}
