package ru.clevertec.util.exceptions;

public class CardNotFoundException extends IllegalArgumentException {
    public CardNotFoundException(String message) {
        super(message);
    }
}
