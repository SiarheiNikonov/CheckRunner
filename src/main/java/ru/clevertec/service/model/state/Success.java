package ru.clevertec.service.model.state;

import lombok.Value;

@Value
public class Success<T> implements Result<T>{
     T data;
}
