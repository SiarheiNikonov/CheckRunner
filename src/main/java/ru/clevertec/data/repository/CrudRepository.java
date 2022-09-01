package ru.clevertec.data.repository;

import ru.clevertec.util.exceptions.RepositoryException;

import java.util.List;

public interface CrudRepository <T>{
    T getById(int id) throws RepositoryException;

    T add(T item) throws RepositoryException;

    boolean remove(Integer id) throws RepositoryException;

    boolean update(T product) throws RepositoryException;

    List<T> findAll(Integer pageSize, long lastItemId) throws RepositoryException;
}
