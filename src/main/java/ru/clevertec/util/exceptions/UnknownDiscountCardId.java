package ru.clevertec.util.exceptions;

public class UnknownDiscountCardId extends IllegalArgumentException{

    public UnknownDiscountCardId(String s) {
        super(s);
    }
}
