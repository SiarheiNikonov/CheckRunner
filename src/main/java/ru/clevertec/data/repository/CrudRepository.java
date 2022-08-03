package ru.clevertec.data.repository;

import ru.clevertec.util.exceptions.RepositoryException;

import java.util.List;

public interface CrudRepository <T>{
    T getById(int id) throws RepositoryException;

    boolean add(T item) throws RepositoryException;

    boolean removeById(Integer id) throws RepositoryException;

    boolean remove(T product) throws RepositoryException;

    boolean update(T product) throws RepositoryException;

    List<T> findAll(Integer pageSize, long lastItemId) throws RepositoryException;
}
