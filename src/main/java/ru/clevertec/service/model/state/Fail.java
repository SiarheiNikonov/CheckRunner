package ru.clevertec.service.model.state;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class Fail<T> implements Result<T> {

    private T data;
    private final String message;
    private final int errorCode;
}
