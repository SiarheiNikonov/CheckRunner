package ru.clevertec.data.model;

public class CheckReceiptItem {

    private final int quantity;
    private final Product product;
    private final long pricePerOneInCents;
    private final long fullPriceInCents;
    private final long discountInCents;

    public int getQuantity() {
        return quantity;
    }

    public Product getProduct() {
        return product;
    }

    public long getPricePerOneInCents() {
        return pricePerOneInCents;
    }

    public long getFullPriceInCents() {
        return fullPriceInCents;
    }

    public long getDiscountInCents() {
        return discountInCents;
    }

    public CheckReceiptItem(int quantity, Product product, long pricePerOneInCents, long fullPriceInCents, long discountInCents) {
        this.quantity = quantity;
        this.product = product;
        this.pricePerOneInCents = pricePerOneInCents;
        this.fullPriceInCents = fullPriceInCents;
        this.discountInCents = discountInCents;
    }
}
