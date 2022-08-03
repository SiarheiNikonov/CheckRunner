package ru.clevertec.data.model;

public class DiscountCard {
    private final Integer id;
    private final DiscountCardType cardType;

    public DiscountCard(Integer id, DiscountCardType cardType) {
        this.id = id;
        this.cardType = cardType;
    }

    public Integer getId() {
        return id;
    }

    public DiscountCardType getCardType() {
        return cardType;
    }
}
