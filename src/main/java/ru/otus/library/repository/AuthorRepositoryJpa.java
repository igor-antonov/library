package ru.otus.library.repository;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.library.domain.Author;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.sql.Date;
import java.util.List;

@SuppressWarnings("JpaQlInspection")
@Repository
public class AuthorRepositoryJpa implements AuthorRepository {

    @PersistenceContext
    EntityManager em;

    @Override
    public Author getById(long id) {
        try{
            return em.find(Author.class, id);
        }
        catch (NoResultException e){
            return null;
        }
    }

    @Override
    public List<Author> getByFirstNameAndSecondName(String firstName, String secondName) throws NoResultException{
        TypedQuery<Author> query = em.createQuery("select a from Author a where " +
                "a.first_name =:firstName and a.secondName =:secondName", Author.class);
        query.setParameter("firstName", firstName);
        query.setParameter("secondName", secondName);
        return query.getResultList();
    }

    @Override
    public List<Author> getBySecondName(String secondName) throws NoResultException {
        TypedQuery<Author> query = em.createQuery("select a from Author a where " +
                "a.secondName =:secondName", Author.class);
        query.setParameter("secondName", secondName);
        return query.getResultList();
    }

    @Override
    public List<Author> getByBirthday(Date birthday) throws NoResultException {
        TypedQuery<Author> query = em.createQuery("select a from Author a where " +
                "a.birthday=:birthday", Author.class);
        query.setParameter("birthday", birthday);
        return query.getResultList();
    }

    @Override
    public List<Author> getAll() {
        return em.createQuery("select a from Author a", Author.class).getResultList();
    }

    @Override
    @Transactional
    public long insert(Author author) {
        em.persist(author);
        return author.getId();
    }

    @Override
    @Transactional
    public void deleteById(long id) {
        em.remove(getById(id));
    }

    @Override
    @Transactional
    public void deleteAll() {
        em.createQuery("delete from Author a").executeUpdate();
    }

    @Override
    @Transactional
    public boolean updateBySecondName(String oldSecondName, String firstName, String secondName, Date birthday) {
         int resultCount = getBySecondName(oldSecondName).size();
         if (resultCount == 1){
             em.createQuery("update Author set firstName =:firstName, secondName =:secondName," +
                  " birthday =:birthday where secondName =:oldSecondName")
                     .setParameter("oldSecondName", oldSecondName)
                     .setParameter("firstName",firstName)
                     .setParameter("secondName",secondName)
                     .setParameter("birthday", birthday)
                     .executeUpdate();
             return true;
         }
         else if (resultCount < 1){
             System.out.println(String.format("Автор с фамилией %s не найден", oldSecondName));
             return false;
         }
         else {
             System.out.println("По указанной фамилии найдено несколько записей. Измените запись по Id:");
             for (Author author : getBySecondName(oldSecondName)){
                 System.out.println(author.getId() + " ");
             }
             return false;
         }
    }

    @Override
    @Transactional
    public boolean updateById(int id, String firstName, String secondName, Date birthday){
        try {
            long authorId = getById(id).getId();
            em.createQuery("update Author set firstName =:firstName, secondName =:secondName," +
                    " birthday =:birthday where id =:id")
                    .setParameter("id", authorId)
                    .setParameter("firstName",firstName)
                    .setParameter("secondName",secondName)
                    .setParameter("birthday", birthday)
                    .executeUpdate();
            return true;
        }
        catch (NoResultException e){
            return false;
        }
    }

    @Override
    public long count() {
        return (long) em.createQuery("select count(id) from Author").getSingleResult();
    }
}
