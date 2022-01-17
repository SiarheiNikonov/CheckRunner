package main.java.data.repository.productrepo;

import main.java.data.model.Product;

public interface ProductRepository {
    Product getProductById(int id);
}
