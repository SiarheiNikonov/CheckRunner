package ru.clevertec.data.model;

public class Product {
    private Integer id;
    private final String title;
    private final int priceInCents;
    private final String description;
    private final Company producer;
    private final long barcode;
    private final boolean onSale;

    public Product(Integer id, String title, int priceInCents, String description, Company producer, long barcode, boolean onSale) {
        this.id = id;
        this.title = title;
        this.priceInCents = priceInCents;
        this.description = description;
        this.producer = producer;
        this.barcode = barcode;
        this.onSale = onSale;
    }
    public Product(String title, int priceInCents, String description, Company producer, long barcode, boolean onSale) {
        this.title = title;
        this.priceInCents = priceInCents;
        this.description = description;
        this.producer = producer;
        this.barcode = barcode;
        this.onSale = onSale;
    }

    public Product(Integer id, String title, int priceInCents) {
        this.id = id;
        this.title = title;
        this.priceInCents = priceInCents;
        this.description = "description";
        this.producer = null;
        this.barcode = -1;
        this.onSale = false;
    }

    public Integer getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public int getPriceInCents() {
        return priceInCents;
    }

    public String getDescription() {
        return description;
    }

    public Company getProducer() {
        return producer;
    }

    public long getBarcode() {
        return barcode;
    }

    public boolean isOnSale() {
        return onSale;
    }
}
