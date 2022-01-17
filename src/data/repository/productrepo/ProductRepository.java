package data.repository.productrepo;

import data.model.Product;

public interface ProductRepository {
    Product getProductById(int id);
}
