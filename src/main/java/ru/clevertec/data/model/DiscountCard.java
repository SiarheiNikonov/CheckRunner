package ru.clevertec.data.model;

public class DiscountCard {
    private int id;
    private DiscountCardType cardType;

    public DiscountCard(int id,DiscountCardType cardType) {
        this.id = id;
        this.cardType = cardType;
    }

    public int getId() {
        return id;
    }

    public DiscountCardType getCardType() {
        return cardType;
    }
}
