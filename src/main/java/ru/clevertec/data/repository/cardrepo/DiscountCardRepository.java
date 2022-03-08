package ru.clevertec.data.repository.cardrepo;

import ru.clevertec.data.model.DiscountCard;

public interface DiscountCardRepository {
    DiscountCard getCardById(int id);
}
