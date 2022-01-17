package main.java.util;

import main.java.data.model.Product;
import java.util.ArrayList;
import java.util.List;

public class ProductGenerator {
    public static List<Product> getProducts() {
        List<Product> products = new ArrayList(50);
        for (int i = 0; i < 50; i++) {
            Long priceInCents = Math.round(Math.random() * 9990 + 10);
            String producer;
            switch (i % 5) {
                case (0):
                    producer = "HP";
                    break;
                case (1):
                    producer = "SAMSUNG";
                    break;
                case (2):
                    producer = "LENOVO";
                    break;
                case (3):
                    producer = "DELL";
                    break;
                default:
                    producer = "APPLE";
            }
            boolean onSale = i % 3 == 0;
            Product product = new Product(
                    i,
                    "Product" + i,
                    priceInCents.intValue(),
                    "description" + i,
                    producer,
                    1234567891112L + i,
                    onSale
            );
            products.add(product);
        }
        return products;
    }
}
