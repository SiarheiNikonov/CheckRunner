package data.repository.productrepo;

import data.model.Product;
import util.ProductGenerator;

import java.util.List;

public class ProductRepositoryImpl implements ProductRepository {
    private List<Product> products;
    private static ProductRepository instance;

    private ProductRepositoryImpl() {
        products = ProductGenerator.getProducts();
    }


    @Override
    public Product getProductById(int id) {
        return products.get(id);
    }

    public static ProductRepository getInstance() {
        if (instance == null) {
            instance = new ProductRepositoryImpl();
        }
        return instance;
    }
}
