package ru.clevertec.data.repository.productrepo;

import ru.clevertec.data.model.Company;
import ru.clevertec.data.model.Product;
import ru.clevertec.data.repository.cardrepo.JdbcDiscountCardRepositoryImpl;
import ru.clevertec.data.repository.companyrepo.CompanyRepo;
import ru.clevertec.util.exceptions.RepositoryException;
import ru.clevertec.util.jdbc.ConnectionPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JdbcProductRepositoryImpl implements ProductRepository {

    private final ConnectionPool connectionPool;
    private final CompanyRepo companyRepo;
    private static volatile JdbcProductRepositoryImpl instance;
    private static final int DEFAULT_PAGE_SIZE = 20;

    private static final String GET_PRODUCT_BY_ID_QUERY =
            "SELECT product_id, title, price_in_cent, description, barcode, on_sale, company_id_ref " +
                    "FROM product  " +
                    "WHERE product_id =?";

    private static final String ADD_PRODUCT_QUERY = "INSERT INTO product (title, price_in_cent, description, company_id_ref, barcode, on_sale)\n " +
            "VALUES (?, ?, ?, ?, ?, ?)";
    private static final String REMOVE_PRODUCT_BY_ID_QUERY = "DELETE FROM product WHERE product_id = ?";
    private static final String UPDATE_PRODUCT_QUERY =
            "UPDATE product " +
                    "SET title = ?, price_in_cent = ?, description = ?, company_id_ref = ?, barcode = ?, on_sale = ? " +
                    "WHERE product_id = ?";

    private static final String FIND_ALL_WITH_PAGING_QUERY =
            "SELECT product_id, title, price_in_cent, description, barcode, on_sale, company_id, company_name, company_address, company_tel_number " +
                    "FROM product JOIN company ON product.company_id_ref = company.company_id " +
                    "WHERE product_id > ? " +
                    "ORDER BY product_id " +
                    "LIMIT ?";


    public JdbcProductRepositoryImpl(ConnectionPool connectionPool, CompanyRepo companyRepo) {
        this.connectionPool = connectionPool;
        this.companyRepo = companyRepo;
    }

    @Override
    public List<Product> findAll(Integer pageSize, long lastItemId) throws RepositoryException {
        try (Connection conn = connectionPool.getConnection()) {
            PreparedStatement statement = conn.prepareStatement(FIND_ALL_WITH_PAGING_QUERY);
            statement.setLong(1, lastItemId);
            int limit = pageSize == null ? DEFAULT_PAGE_SIZE : pageSize;
            statement.setInt(2, limit);
            List<Product> products = new ArrayList<>(limit);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int productId = resultSet.getInt("product_id");
                String title = resultSet.getString("title");
                int priceInCents = resultSet.getInt("price_in_cent");
                String description = resultSet.getString("description");
                long barcode = resultSet.getLong("barcode");
                boolean onSale = resultSet.getBoolean("on_sale");
                int companyId = resultSet.getInt("company_id");
                String companyName = resultSet.getString("company_name");
                String companyAddress = resultSet.getString("company_address");
                String companyTelNumber = resultSet.getString("company_tel_number");

                Company company = new Company(companyId, companyName, companyAddress, companyTelNumber);
                products.add(new Product(productId, title, priceInCents, description, company, barcode, onSale));
            }
            return products;
        } catch (SQLException e) {
            throw new RepositoryException(e, "Something went wrong");
        }
    }

    @Override
    public Product getById(int id) throws RepositoryException {
        try (Connection conn = connectionPool.getConnection()) {
            PreparedStatement statement = conn.prepareStatement(GET_PRODUCT_BY_ID_QUERY);
            statement.setInt(1, id);
            ResultSet result = statement.executeQuery();
            Product product = null;
            if (result.next()) {
                int productId = result.getInt(1);
                String title = result.getString(2);
                int priceInCents = result.getInt(3);
                String description = result.getString(4);
                long barcode = result.getLong(5);
                boolean onSale = result.getBoolean(6);
                int companyId = result.getInt(7);

                Company company = companyRepo.getCompanyById(companyId);
                product = new Product(productId, title, priceInCents, description, company, barcode, onSale);
            }
            return product;
        } catch (SQLException e) {
            throw new RepositoryException(e, "Something went wrong");
        }
    }

    @Override
    public boolean add(Product product) throws RepositoryException {
        return addOrUpdate(product, false);
    }

    @Override
    public boolean removeById(Integer id) throws RepositoryException {
        try (Connection conn = connectionPool.getConnection()) {
            PreparedStatement statement = conn.prepareStatement(REMOVE_PRODUCT_BY_ID_QUERY);
            statement.setInt(1, id);
            int changedRows = statement.executeUpdate();
            return changedRows == 1;
        } catch (SQLException e) {
            throw new RepositoryException(e, "Something went wrong");
        }
    }

    @Override
    public boolean remove(Product product) throws RepositoryException {
        return removeById(product.getId());
    }

    @Override
    public boolean update(Product product) throws RepositoryException {
        return addOrUpdate(product, true);
    }

    private boolean addOrUpdate(Product product, boolean update) throws RepositoryException {
        try (Connection connection = connectionPool.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(update ? UPDATE_PRODUCT_QUERY : ADD_PRODUCT_QUERY);
            statement.setString(1, product.getTitle());
            statement.setInt(2, product.getPriceInCents());
            statement.setString(3, product.getDescription());
            statement.setInt(4, product.getProducer().getCompanyId());
            statement.setLong(5, product.getBarcode());
            statement.setBoolean(6, product.isOnSale());
            if (update) statement.setInt(7, product.getId());
            int changedRows = statement.executeUpdate();
            return changedRows == 1;
        } catch (SQLException e) {
            throw new RepositoryException(e, "Something went wrong");
        }
    }

    // Не знаю правильно ли в одну репу пихать другую (используется для получения компании, используемой для создания объекта).
    // Или нужно было тянуть данные из SQL и самому создавать объект Company?
    // Пусть будет.
    public static JdbcProductRepositoryImpl getInstance(ConnectionPool pool, CompanyRepo companyRepo) {
        if (instance == null)
            synchronized (JdbcDiscountCardRepositoryImpl.class) {
                if (instance == null)
                    instance = new JdbcProductRepositoryImpl(pool, companyRepo);
            }
        return instance;
    }

}
