package ru.clevertec.service;

import ru.clevertec.data.model.state.Result;
import ru.clevertec.util.exceptions.RepositoryException;

import java.util.List;

public interface CrudService <T>{
    Result<T> getById(String id) throws RepositoryException;

    Result<Boolean> add(T item);

    Result<Boolean> removeById(String id);

    Result<Boolean> remove(T item);

    Result<Boolean> update(T item);

    Result<List<T>> findAll(String pageSize, String lastItemId);
}
