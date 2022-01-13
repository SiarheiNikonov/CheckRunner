import java.util.ArrayList;
import java.util.List;

public class ProductRepositoryImpl implements ProductRepository {
    private List<Product> products = new ArrayList<>();
    private static ProductRepository instance;

    private ProductRepositoryImpl() {
        for (int i = 0; i < 50; i++) {
            Long priceInCents = Math.round(Math.random() * 9990 + 10);
            String producer;
            switch (i%5) {
                case(0):producer = "HP";
                case(1):producer = "SAMSUNG";
                case(2):producer = "LENOVO";
                case(3):producer = "DELL";
                default:producer = "APPLE";
            }
            boolean onSale = i % 3 == 0;
            Product product = new Product(
                    i,
                    "Product" + i,
                    priceInCents.intValue(),
                    "Description" + i,
                    producer,
                    1234567891112L+i,
                    onSale
            );
            products.add(product);
        }
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
