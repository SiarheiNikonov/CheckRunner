package ru.clevertec.util.exceptions;

import java.io.IOException;

public class RepositoryException extends IOException {
    public RepositoryException(Throwable e, String message) {
        super(message, e);
    }
}
