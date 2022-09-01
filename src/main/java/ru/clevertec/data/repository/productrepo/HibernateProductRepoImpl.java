package ru.clevertec.data.repository.productrepo;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.clevertec.data.model.Product;
import ru.clevertec.data.repository.CrudRepository;
import ru.clevertec.util.exceptions.RepositoryException;

import java.util.List;


interface HibernateProductRepoImpl extends JpaRepository<Product, Long> {

    @Query("from Product p INNER JOIN fetch p.producer WHERE p.id > :lastItemId ORDER BY p.id ASC")
    List<Product> findAll(Pageable pageable, @Param("lastItemId") long lastItemId) throws RepositoryException;
}
