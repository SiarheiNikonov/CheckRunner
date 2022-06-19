package ru.clevertec.util.exceptions;

public class UnknownProductIdException extends IllegalArgumentException{
    public UnknownProductIdException(String s) {
        super(s);
    }
}
