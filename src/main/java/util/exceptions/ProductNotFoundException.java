package main.java.util.exceptions;

public class ProductNotFoundException extends IllegalArgumentException {
    public ProductNotFoundException(String message) {
        super(message);
    }
}
