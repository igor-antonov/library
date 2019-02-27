package ru.otus.library.repository;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.library.domain.Author;
import ru.otus.library.domain.Book;
import ru.otus.library.domain.Genre;
import ru.otus.library.exception.DataNotFoundException;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@SuppressWarnings("JpaQlInspection")
@Repository
public class BookRepositoryJpa implements BookRepository {

    @PersistenceContext
    EntityManager em;

    @Override
    @Transactional
    public Book getById(long id) throws DataNotFoundException {
        try{
            return em.find(Book.class, id);
        }
        catch (NoResultException e){
            throw new DataNotFoundException("Результаты не найдены");
        }
    }

    @Override
    public List<Book> getByTitle(String title) {
        TypedQuery<Book> query =
                em.createQuery("select b from Book b where b.title =:title", Book.class);
        query.setParameter("title", title);
        return query.getResultList();
    }

    @Override
    public List<Book> getByAuthor(Author author) {
        TypedQuery<Book> query =
                em.createQuery("select b from Book b where b.author.id = :id", Book.class);
        query.setParameter("id", author.getId());
        return query.getResultList();
    }

    @Override
    public List<Book> getByGenre(Genre genre) {
        TypedQuery<Book> query =
                em.createQuery("select b from Book b where b.genre.id = :id", Book.class);
        query.setParameter("id", genre.getId());
        return query.getResultList();
    }

    @Override
    public List<Book> getAll() {
        return em.createQuery("select b from Book b", Book.class).getResultList();
    }

    @Override
    @Transactional
    public long insert(Book book) {
        em.persist(book);
        return book.getId();
    }

    @Override
    @Transactional
    public boolean deleteByTitle(String title) throws NoResultException{
        for (Book book: getByTitle(title)){
            em.refresh(book);
            em.remove(book);
        }
        return true;
    }

    @Override
    @Transactional
    public boolean deleteAll() {
        for (Book book: getAll()){
            em.refresh(book);
            em.remove(book);
        }
        return true;
    }

    @Override
    @Transactional
    public boolean updateById(long id, Book book) {
        try {
            long bookId = getById(id).getId();
            em.createQuery("update Book b set b.title =:title, b.author =:author," +
                    " b.genre =:genre where id =:id")
                    .setParameter("id", bookId)
                    .setParameter("title", book.getTitle())
                    .setParameter("author", book.getAuthor())
                    .setParameter("genre", book.getGenre())
                    .executeUpdate();
            return true;
        }
        catch (DataNotFoundException e){
            return false;
        }
    }

    @Override
    public long count() {
        return (long) em.createQuery("select count(id) from Book").getSingleResult();
    }
}
