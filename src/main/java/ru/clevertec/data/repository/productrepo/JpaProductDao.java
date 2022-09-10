package ru.clevertec.data.repository.productrepo;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.clevertec.data.model.Product;

import java.util.List;


interface JpaProductDao extends JpaRepository<Product, Long> {

    @Query("from Product p INNER JOIN fetch p.producer WHERE p.id > :lastItemId ORDER BY p.id ASC")
    List<Product> findAll(Pageable pageable, @Param("lastItemId") long lastItemId);
}
