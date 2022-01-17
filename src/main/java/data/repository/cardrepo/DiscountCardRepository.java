package main.java.data.repository.cardrepo;

import main.java.data.model.DiscountCard;

public interface DiscountCardRepository {
    DiscountCard getCardById(int id);
}
