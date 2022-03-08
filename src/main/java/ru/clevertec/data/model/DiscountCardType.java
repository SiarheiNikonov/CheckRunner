package ru.clevertec.data.model;

public enum DiscountCardType {
    WOODEN(1),
    SILVER(3),
    GOLD(5),
    PLATINUM(10);
    private int discount;

    DiscountCardType(int discount) {
        this.discount = discount;
    }

    public int getDiscount() {
        return discount;
    }
}
