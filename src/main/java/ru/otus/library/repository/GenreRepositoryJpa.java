package ru.otus.library.repository;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.library.domain.Genre;

import javax.persistence.*;
import java.util.List;

@Repository
@SuppressWarnings("JpaQlInspection")
public class GenreRepositoryJpa implements GenreRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Genre getById(long id) {
        try{
            return em.find(Genre.class, id);
        }
        catch (NoResultException e){
            return null;
        }
    }

    @Override
    public Genre getByName(String name) throws NoResultException {
        TypedQuery<Genre> query = em.createQuery("select g from Genre g where g.name =:name", Genre.class);
        query.setParameter("name",name);
        return query.getSingleResult();
    }

    @Override
    public List<Genre> getAll() {
        TypedQuery<Genre> query = em.createQuery("select g from Genre g", Genre.class);
        return query.getResultList();
    }

    @Override
    @Transactional
    public long insert(Genre genre) {
        em.persist(genre);
        return genre.getId();
    }

    @Override
    @Transactional
    public boolean deleteByName(String name) throws NoResultException{
        em.remove(getByName(name));
        return true;
    }

    @Override
    @Transactional
    public boolean deleteAll(){
        em.createQuery("delete from Genre g").executeUpdate();
        return true;
    }

    @Override
    @Transactional
    public boolean updateByName(String oldName, String newName) throws NoResultException {
        String name = getByName(oldName).getName();
        em.createQuery("update Genre set genre_name =:newName " +
                "where genre_name =:oldName")
                .setParameter("newName",newName)
                .setParameter("oldName",name)
                .executeUpdate();
        return true;
    }

    @Override
    public long count() {
        return (long) em.createQuery("select count(id) from Genre").getSingleResult();
    }
}
