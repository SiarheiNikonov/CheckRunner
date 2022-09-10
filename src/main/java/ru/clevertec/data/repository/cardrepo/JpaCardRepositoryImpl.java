package ru.clevertec.data.repository.cardrepo;

import lombok.RequiredArgsConstructor;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import ru.clevertec.data.model.DiscountCard;
import ru.clevertec.util.exceptions.RepositoryException;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class JpaCardRepositoryImpl implements DiscountCardRepository {

    private final JpaCardDao jpaCardDao;

    @Override
    public DiscountCard getById(Long id) throws RepositoryException {
        return jpaCardDao.findById(id).orElseThrow(
                () -> new RepositoryException("Card not found.")
        );
    }

    @Override
    public DiscountCard add(DiscountCard item) throws RepositoryException {
        return jpaCardDao.save(item);
    }

    @Override
    public boolean remove(Long id) throws RepositoryException {
        jpaCardDao.deleteById(id);
        return true;
    }

    @Override
    public boolean update(DiscountCard card) throws RepositoryException {
        jpaCardDao.save(card);
        return true;
    }

    @Override
    public List<DiscountCard> findAll(Integer pageSize, long lastItemId) throws RepositoryException {
        return jpaCardDao.findAll(Pageable.ofSize(pageSize), lastItemId);
    }
}
