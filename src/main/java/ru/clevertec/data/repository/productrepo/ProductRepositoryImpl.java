package ru.clevertec.data.repository.productrepo;

import ru.clevertec.data.model.Product;
import ru.clevertec.util.ProductGenerator;
import ru.clevertec.util.exceptions.UnknownProductIdException;

import java.util.List;

public class ProductRepositoryImpl implements ProductRepository {
    private List<Product> products;
    private static ProductRepository instance;

    private ProductRepositoryImpl() {
        products = ProductGenerator.getProducts();
    }


    @Override
    public Product getProductById(int id) {
        if(id <= 0) throw new UnknownProductIdException("Unknown product id");
        return products.get(id - 1);
    }

    public static ProductRepository getInstance() {
        if (instance == null) {
            instance = new ProductRepositoryImpl();
        }
        return instance;
    }
}
