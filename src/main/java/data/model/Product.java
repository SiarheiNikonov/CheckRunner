package data.model;

public class Product {
    private int id;
    private String title;
    private int priceInCents;
    private String description;
    private String producer;
    private long barcode;
    private boolean onSale;

    public Product(int id, String title, int priceInCents, String description, String producer, long barcode, boolean onSale) {
        this.id = id;
        this.title = title;
        this.priceInCents = priceInCents;
        this.description = description;
        this.producer = producer;
        this.barcode = barcode;
        this.onSale = onSale;
    }

    public Product(int id, String title, int priceInCents) {
        this.id = id;
        this.title = title;
        this.priceInCents = priceInCents;
        this.description = "description";
        this.producer = "producer";
        this.barcode = -1;
        this.onSale = false;
    }

    public int getId() {
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

    public String getProducer() {
        return producer;
    }

    public long getBarcode() {
        return barcode;
    }

    public boolean isOnSale() {
        return onSale;
    }
}
