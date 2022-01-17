package data.repository.cardrepo;

import data.model.DiscountCard;

public interface DiscountCardRepository {
    DiscountCard getCardById(int id);
}
