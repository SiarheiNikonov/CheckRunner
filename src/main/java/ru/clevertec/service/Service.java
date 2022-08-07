package ru.clevertec.service;

import ru.clevertec.data.model.state.Result;

public interface Service{

    Result<String> handleRequest(String data, RequestMethod method);
}
