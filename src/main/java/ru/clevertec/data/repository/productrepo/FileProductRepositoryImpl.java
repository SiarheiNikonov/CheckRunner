package ru.clevertec.data.repository.productrepo;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import ru.clevertec.data.model.Product;
import ru.clevertec.util.Constants;
import ru.clevertec.util.exceptions.CardNotFoundException;
import ru.clevertec.util.exceptions.ProductNotFoundException;
import ru.clevertec.util.exceptions.RepositoryInitializationException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

public class FileProductRepositoryImpl implements ProductRepository {

    private List<Product> products;
    private static FileProductRepositoryImpl instance;

    private FileProductRepositoryImpl() throws RepositoryInitializationException {
        Moshi moshi = (new Moshi.Builder()).build();
        Type type = Types.newParameterizedType(List.class, Product.class);
        JsonAdapter<List<Product>> adapter = moshi.adapter(type);
        try (BufferedReader reader = new BufferedReader(new FileReader(Constants.PRODUCT_FILE_NAME))) {
            String string = reader.readLine();
            products = adapter.fromJson(string);
        } catch (IOException e) {
            throw new RepositoryInitializationException(e.getMessage());
        }
    }

    @Override
    public Product getProductById(int id) throws ProductNotFoundException {
        //Кривая переделка. В цикле могли найти первое совпадение и вернуть его.
        //В данном случае сначала фильтруются ВСЕ элементы, потом проверяется не попался ли такой.
        return products.stream().filter(product -> product.getId() == id).findFirst().orElseThrow(() ->
                new ProductNotFoundException(Constants.UNKNOWN_PRODUCT_MESSAGE));
    }


    public static FileProductRepositoryImpl getInstance() throws RepositoryInitializationException {
        if (instance == null) instance = new FileProductRepositoryImpl();
        return instance;
    }
}
