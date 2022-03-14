package ru.clevertec.data.repository.productrepo;


import ru.clevertec.data.model.Product;

public interface ProductRepository {
    Product getProductById(int id);
}
