package ru.clevertec.data.repository.cardrepo;

import lombok.RequiredArgsConstructor;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import ru.clevertec.data.model.DiscountCard;
import ru.clevertec.util.exceptions.RepositoryException;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class HibernateCardRepositoryImpl implements DiscountCardRepository {

    private final SessionFactory sessionFactory;

    @Override
    public DiscountCard getById(Long id) throws RepositoryException {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            DiscountCard card = session.get(DiscountCard.class, id);
            session.getTransaction().commit();
            return card;
        } catch (HibernateException e) {
            e.printStackTrace();
            throw new RepositoryException(e, e.getMessage());
        }

    }

    @Override
    public DiscountCard add(DiscountCard item) throws RepositoryException {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Long id = (Long) session.save(item);
            session.getTransaction().commit();
            item.setId(id);
            return item;
        } catch (HibernateException e) {
            throw new RepositoryException(e, e.getMessage());
        }
    }

    @Override
    public boolean remove(Long id) throws RepositoryException {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            DiscountCard card = session.get(DiscountCard.class, id);
            session.delete(card);
            session.getTransaction().commit();
            return true;
        } catch (HibernateException e) {
            throw new RepositoryException(e, e.getMessage());
        }
    }

    @Override
    public boolean update(DiscountCard card) throws RepositoryException {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.update(card);
            session.getTransaction().commit();
            return true;
        } catch (HibernateException e) {
            throw new RepositoryException(e, e.getMessage());
        }
    }

    @Override
    public List<DiscountCard> findAll(Integer pageSize, long lastItemId) throws RepositoryException {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Query<DiscountCard> query = session.createQuery(
                    "from DiscountCard d INNER JOIN fetch d.cardType WHERE d.id > :lastItemId ORDER BY d.id ASC"
            );
            query.setParameter("lastItemId", lastItemId);
           query.setMaxResults(pageSize);
            List<DiscountCard> result = query.list();
            session.getTransaction().commit();
            return result;
        } catch (HibernateException e) {
            throw new RepositoryException(e, e.getMessage());
        }
    }
}
