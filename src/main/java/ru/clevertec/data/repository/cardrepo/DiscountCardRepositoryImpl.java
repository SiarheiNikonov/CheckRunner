package ru.clevertec.data.repository.cardrepo;


import ru.clevertec.data.model.DiscountCard;
import ru.clevertec.data.model.DiscountCardType;
import ru.clevertec.util.exceptions.UnknownDiscountCardId;

public class DiscountCardRepositoryImpl implements DiscountCardRepository {

    @Override
    public DiscountCard getCardById(int id) {
        if(id < 1) throw new UnknownDiscountCardId("Unknown card id");
        switch (id % 4) {
            case (1):
                return new DiscountCard(id, DiscountCardType.WOODEN);
            case (2):
                return new DiscountCard(id, DiscountCardType.SILVER);
            case (3):
                return new DiscountCard(id, DiscountCardType.GOLD);
            default: return new DiscountCard(id, DiscountCardType.PLATINUM);
        }
    }
}
