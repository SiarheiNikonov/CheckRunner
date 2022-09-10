package ru.clevertec.data.repository.cardrepo;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.clevertec.data.model.DiscountCard;
import ru.clevertec.data.model.Product;

import java.util.List;

interface JpaCardDao extends JpaRepository<DiscountCard, Long> {

    @Query("from DiscountCard d INNER JOIN fetch d.cardType WHERE d.id > :lastItemId ORDER BY d.id ASC")
    List<DiscountCard> findAll(Pageable pageable, @Param("lastItemId") long lastItemId);

}
