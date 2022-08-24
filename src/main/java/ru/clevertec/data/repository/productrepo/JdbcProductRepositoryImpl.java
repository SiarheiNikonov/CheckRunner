package ru.clevertec.data.repository.productrepo;

import ru.clevertec.data.model.Product;
import ru.clevertec.data.repository.cardrepo.JdbcDiscountCardRepositoryImpl;
import ru.clevertec.util.exceptions.ProductNotFoundException;
import ru.clevertec.util.exceptions.RepositoryInitializationException;
import ru.clevertec.util.jdbc.ConnectionPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JdbcProductRepositoryImpl implements ProductRepository {
    private static final String GET_PRODUCT_BY_ID_QUERY =
                    "SELECT product_id, title, price_in_cent, description, barcode, on_sale, company_name " +
                    "FROM product  " +
                    "JOIN company ON product.company_id_ref = company.company_id " +
                    "WHERE product_id =?";
    private final ConnectionPool connectionPool;

    private static volatile JdbcProductRepositoryImpl instance;

    private JdbcProductRepositoryImpl(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    @Override
    public Product getProductById(int id) {
        try (Connection conn = connectionPool.getConnection()) {
            PreparedStatement statement = conn.prepareStatement(GET_PRODUCT_BY_ID_QUERY);
            statement.setInt(1, id);
            ResultSet result = statement.executeQuery();
            if (result.next()) {
                int productId = result.getInt(1);
                String title = result.getString(2);
                int priceInCents = result.getInt(3);
                String description = result.getString(4);
                long barcode = result.getLong(5);
                boolean onSale = result.getBoolean(6);
                String companyName = result.getString(7);
                Product product = new Product(productId, title, priceInCents, description, companyName, barcode, onSale);
                return product;
            } else throw new ProductNotFoundException(String.format("Product with id = %s not found", id));

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static JdbcProductRepositoryImpl getInstance(ConnectionPool pool) throws RepositoryInitializationException {
        if (instance == null)
            synchronized (JdbcDiscountCardRepositoryImpl.class) {
                if (instance == null)
                    instance = new JdbcProductRepositoryImpl(pool);
            }
        return instance;
    }

}
