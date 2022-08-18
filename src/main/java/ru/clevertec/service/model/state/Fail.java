package ru.clevertec.service.model.state;

public class Fail<T> implements Result<T> {

    private T data;
    private final String message;

    private final int errorCode;
    public int getErrorCode() {
        return errorCode;
    }



    public Fail(T data, String message, int errorCode) {
        this(message, errorCode);
        this.data = data;
    }

    public Fail(String message, int errorCode) {
        this.message = message;
        this.errorCode = errorCode;
    }

    public T getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }
}
