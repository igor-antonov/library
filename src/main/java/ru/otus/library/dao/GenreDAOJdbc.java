package ru.otus.library.dao;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.otus.library.domain.Genre;
import ru.otus.library.domain.GenreMapper;
import java.util.List;

@Repository
public class GenreDAOJdbc implements GenreDAO {

    private final JdbcOperations jdbc;
    private final NamedParameterJdbcOperations parameterJdbc;

    public GenreDAOJdbc(JdbcOperations jdbcOperations, NamedParameterJdbcOperations parameterJdbc){
        this.jdbc = jdbcOperations;
        this.parameterJdbc = parameterJdbc;
    }

    @Override
    public Genre getById(int id) {
        try {
            MapSqlParameterSource params = new MapSqlParameterSource();
            params.addValue("id", id);
            return parameterJdbc.queryForObject("select * from genres where id = :id",
                    params, new GenreMapper());
        }
        catch (EmptyResultDataAccessException e){
            return null;
        }
    }

    @Override
    public Genre getByName(String name) {
        try {
            MapSqlParameterSource params = new MapSqlParameterSource();
            params.addValue("name", name);
            return parameterJdbc.queryForObject("select * from genres where genre_name =:name",
                    params, new GenreMapper());
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public List<Genre> getAll() {
        return jdbc.query("select * from genres", new GenreMapper());
    }

    @Override
    public int insert(Genre genre) {
        KeyHolder key = new GeneratedKeyHolder();
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("genre", genre.getName());
        parameterJdbc.update("insert into genres (genre_name) values (:genre)", params, key);
        return key.getKey().intValue();
    }

    @Override
    public void deleteByName(String name){
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("name", name);
        parameterJdbc.update("delete from genres where genre_name =:name", params);
    }

    @Override
    public void deleteAll(){
        jdbc.update("delete from genres");
    }

    @Override
    public void updateByName(String oldName, String newName) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("oldName", oldName);
        params.addValue("newName", newName);
        parameterJdbc.update("update genres set genre_name =:newName where genre_name =:oldName",
                params);
    }

    @Override
    public int count() {
        return jdbc.queryForObject("select count(*) from genres", Integer.class);
    }
}
