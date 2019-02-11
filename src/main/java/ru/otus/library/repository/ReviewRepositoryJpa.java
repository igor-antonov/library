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
        System.out.println(book.getId());
        TypedQuery<Review> query = em.createQuery("select r from Review r, Book b " +
                "where r.book = b.id and b.id =:book_id", Review.class).setParameter("book_id", book.getId());
        return query.getResultList();
    }

    @Override
    @Transactional
    public long insert(Review review) {
        em.merge(review);
        return em.merge(review).getId();
    }

    @Override
    @Transactional
    public void deleteAll() {
        em.createQuery("delete from Review r").executeUpdate();
    }

    @Override
    @Transactional
    public boolean updateById(long id, Review review) {
        try {
            long reviewId = getById(id).getId();
            em.createQuery("update Review r set r.book =:bookId, r.text =:text," +
                    " r.creationDate =:creationDate, reviewer =:reviewer where id =:id")
                    .setParameter("id", reviewId)
                    .setParameter("text", review.getText())
                    .setParameter("creationDate", review.getCreationDate())
                    .setParameter("bookId", review.getBook().getId())
                    .setParameter("reviewer", review.getReviewer())
                    .executeUpdate();
            return true;
        }
        catch (NoResultException e){
            return false;
        }
    }
}