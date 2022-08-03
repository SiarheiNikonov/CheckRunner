package ru.clevertec.data.model.state;

public class Success<T> implements Result<T>{
    private final T data;

    public Success(T data) {
        this.data = data;
    }

    public T getData() {
        return data;
    }
}
