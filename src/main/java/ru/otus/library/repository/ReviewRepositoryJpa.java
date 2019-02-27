package ru.otus.library.repository;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.library.domain.Book;
import ru.otus.library.domain.Review;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
@SuppressWarnings("JpaQlInspection")
public class ReviewRepositoryJpa implements ReviewRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Review getById(long id) {
        try{
            return em.find(Review.class, id);
        }
        catch (NoResultException e){
            return null;
        }
    }

    @Override
    @Transactional
    public List<Review> getByBook(Book book) {
        TypedQuery<Review> query = em.createQuery("select r from Review r " +
                "where r.book  =:book", Review.class).setParameter("book", book);
        return query.getResultList();
    }

    @Override
    public List<Review> getAll() {
        return em.createQuery("select r from Review r", Review.class).getResultList();
    }

    @Override
    @Transactional
    public long insert(Review review){
        em.merge(review);
        return em.merge(review).getId();
    }

    @Override
    @Transactional
    public boolean deleteAll() {
        em.createQuery("delete from Review r").executeUpdate();
        for (Review review: getAll()){
            em.refresh(review);
        }
        return true;
    }

    @Override
    @Transactional
    public boolean updateById(long id, Review review) {
        try {
            long reviewId = getById(id).getId();
            em.createQuery("update Review r set r.book =:book, r.text =:text," +
                    " r.creationDate =:creationDate, reviewer =:reviewer where id =:id")
                    .setParameter("id", reviewId)
                    .setParameter("text", review.getText())
                    .setParameter("creationDate", review.getCreationDate())
                    .setParameter("book", review.getBook())
                    .setParameter("reviewer", review.getReviewer())
                    .executeUpdate();
            return true;
        }
        catch (NoResultException e){
            return false;
        }
    }

    @Override
    public long count() {
        return (long) em.createQuery("select count(id) from Review").getSingleResult();
    }
}