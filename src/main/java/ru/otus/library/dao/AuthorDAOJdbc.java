package ru.otus.library.dao;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.otus.library.domain.Author;
import ru.otus.library.domain.AuthorMapper;
import java.sql.Date;
import java.util.List;

@Repository
public class AuthorDAOJdbc implements AuthorDAO {

    private final JdbcOperations jdbc;
    private final NamedParameterJdbcOperations parameterJdbc;

    public AuthorDAOJdbc(JdbcOperations jdbcOperations, NamedParameterJdbcOperations parameterOperations){
        this.jdbc = jdbcOperations;
        this.parameterJdbc = parameterOperations;
    }

    @Override
    public Author getById(int id) {
        try{
            MapSqlParameterSource params = new MapSqlParameterSource();
            params.addValue("id", id);
            return parameterJdbc.queryForObject("select * from authors where id =:id",
                    params, new AuthorMapper());
        }
        catch (EmptyResultDataAccessException e){
            return null;
        }
    }

    @Override
    public List<Author> getByFirstNameAndSecondName(String firstName, String secondName) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("firstName", firstName);
        params.addValue("secondName", secondName);
        return parameterJdbc.query("select * from authors " +
                        "where first_name =:firstName and second_name =:secondName",
                params, new AuthorMapper());
    }

    @Override
    public List<Author> getBySecondName(String secondName) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("secondName", secondName);
        return parameterJdbc.query("select * from authors " +
                        "where second_name =:secondName",
                params, new AuthorMapper());
    }

    @Override
    public List<Author> getByBirthday(Date birthday) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("birthday", birthday);
        return parameterJdbc.query("select * from authors where birthday=:birthday",
                params, new AuthorMapper());
    }

    @Override
    public List<Author> getAll() {
        return jdbc.query("select * from authors", new AuthorMapper());
    }

    @Override
    public int insert(String firstName, String secondName, Date birthday) {
        KeyHolder key = new GeneratedKeyHolder();
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("firstName", firstName);
        params.addValue("secondName", secondName);
        params.addValue("birthday", birthday);
        parameterJdbc.update("insert into authors (first_name, second_name, birthday) " +
                "values (:firstName ,:secondName ,:birthday)", params, key);
        return key.getKey().intValue();
    }

    @Override
    public void deleteById(int id) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", id);
        parameterJdbc.update("delete from authors where id =:id", params);
    }

    @Override
    public void deleteAll() {
        jdbc.update("delete from authors");
    }

    @Override
    public int updateBySecondName(String oldSecondName, String firstName, String secondName, Date birthday) {
        if (getBySecondName(oldSecondName).size()>0){
            System.out.println("По указанной фамилии найдено несколько записей. Измените запись по Id:");
            for (Author author : getBySecondName(oldSecondName)){
                System.out.println(author.getId() + " ");
            }
            return -1;
        }
        else{
            MapSqlParameterSource params = new MapSqlParameterSource();
            params.addValue("firstName", firstName);
            params.addValue("secondName", secondName);
            params.addValue("birthday", birthday);
            params.addValue("oldSecondName", secondName);
            return parameterJdbc.update("update authors set first_name =:firstName, second_name =:secondName," +
                    " birthday =:birthday where second_name =:oldSecondName", params);
        }
    }

    @Override
    public int updateById(int id, String firstName, String secondName, Date birthday) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("firstName", firstName);
        params.addValue("secondName", secondName);
        params.addValue("birthday", birthday);
        params.addValue("id", id);
        return parameterJdbc.update("update authors set first_name =:firstName, second_name =:secondName," +
             " birthday =:birthday where id =:id", params);
    }

    @Override
    public int count() {
        return jdbc.queryForObject("select count(*) from authors", Integer.class);
    }
}
