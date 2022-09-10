package ru.clevertec.service.product;

import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.clevertec.data.model.Product;
import ru.clevertec.service.model.state.Fail;
import ru.clevertec.service.model.state.Result;
import ru.clevertec.service.model.state.Success;
import ru.clevertec.data.repository.CrudRepository;
import ru.clevertec.data.repository.productrepo.ProductRepository;
import ru.clevertec.service.RequestMethod;
import ru.clevertec.util.exceptions.RepositoryException;

import java.util.List;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private static final String PROBLEM_WITH_DB_MESSAGE = "Problem with db has occurred";
    private static final String NEGATIVE_ID_MESSAGE = "Product id = %d is not valid (negative or zero)";
    private static final String WRONG_ID_FORMAT_MESSAGE = "Wrong id format (%s)";
    private static final String PRODUCT_WITH_ID_NOT_FOUND_MESSAGE = "Product with id = %d not found";
    private static final String UNSUCCESSFUL_OPERATION_MESSAGE = "Unsuccessful product %s operation (id = %d)";
    private static final String WRONG_PAGE_OR_LAST_ID_MESSAGE = "Wrong page size or last id format";
    private static final String NEGATIVE_PAGE_OR_LAST_ID_MESSAGE = "%s = %d is not valid (negative or zero)";
    private static final String EMPTY_PRODUCT_LIST_MESSAGE = "There are no products!";
    private static final String PAGING_REGEX = "^last_index=[1-9]+\\d*&page_size=[1-9]+\\d*$";
    private static final int SERVER_ERROR_CODE = 500;
    private static final int REQUEST_ERROR_CODE = 400;

    private final CrudRepository<Product> repo;

    @Override
    public Result<String> handleRequest(String data, RequestMethod method) {
        switch (method) {
            case GET: {
                return handleGetRequest(data);
            }
            case PUT: {
                Product product = new Gson().fromJson(data, Product.class);
                return update(product);
            }
            case POST: {
                Product product = new Gson().fromJson(data, Product.class);
                return add(product);
            }
            case DELETE: {
                return removeById(data.split("=")[1]);
            }
        }
        return new Fail<>("Unknown method", 400);
    }

    private Result<String> getById(String idText) {
        long id;
        try {
            id = Long.parseLong(idText);
        } catch (NumberFormatException e) {
            return new Fail<>(String.format(WRONG_ID_FORMAT_MESSAGE, idText), REQUEST_ERROR_CODE);
        }
        if (isNumberNegativeOrZero(id)) return new Fail<>(String.format(NEGATIVE_ID_MESSAGE, id), REQUEST_ERROR_CODE);
        Product product;
        try {
            product = repo.getById(id);
        } catch (RepositoryException e) {
            return new Fail<>(PROBLEM_WITH_DB_MESSAGE, SERVER_ERROR_CODE);
        }
        if (product == null)
            return new Fail<>(String.format(PRODUCT_WITH_ID_NOT_FOUND_MESSAGE, id), REQUEST_ERROR_CODE);
        String result = new Gson().toJson(product);
        return new Success<>(result);
    }

    private Result<String> add(Product product) {
        Product result;
        try {
            result = repo.add(product);
        } catch (RepositoryException e) {
            return new Fail<>(PROBLEM_WITH_DB_MESSAGE, SERVER_ERROR_CODE);
        }
        if (result != null) return new Success<>("Product has been saved");
        else
            return new Fail<>(String.format(UNSUCCESSFUL_OPERATION_MESSAGE, "adding", product.getId()), REQUEST_ERROR_CODE);
    }

    private Result<String> removeById(String idText) {
        int id;
        try {
            id = Integer.parseInt(idText);
        } catch (NumberFormatException e) {
            return new Fail<>(String.format(WRONG_ID_FORMAT_MESSAGE, idText), REQUEST_ERROR_CODE);
        }
        return removeProductById(id);
    }

    private Result<String> update(Product product) {
        boolean result;
        try {
            result = repo.update(product);
        } catch (RepositoryException e) {
            return new Fail<>(PROBLEM_WITH_DB_MESSAGE, SERVER_ERROR_CODE);
        }
        if (result) return new Success<>("Product has been updated");
        else
            return new Fail<>(String.format(UNSUCCESSFUL_OPERATION_MESSAGE, "updating", product.getId()), REQUEST_ERROR_CODE);
    }

    private Result<String> findAll(String pageSizeText, String lastItemIdText) {
        int pageSize;
        int lastItemId;
        try {
            pageSize = Integer.parseInt(pageSizeText);
            lastItemId = Integer.parseInt(lastItemIdText);
        } catch (NumberFormatException e) {
            return new Fail<>(WRONG_PAGE_OR_LAST_ID_MESSAGE, REQUEST_ERROR_CODE);
        }
        if (isNumberNegativeOrZero(pageSize)) {
            return new Fail<>(String.format(NEGATIVE_PAGE_OR_LAST_ID_MESSAGE, "Page", pageSize), REQUEST_ERROR_CODE);
        }
        if (isNumberNegativeOrZero(lastItemId)) {
            return new Fail<>(String.format(NEGATIVE_PAGE_OR_LAST_ID_MESSAGE, "Last item id", pageSize), REQUEST_ERROR_CODE);
        }
        List<Product> products;
        try {
            products = repo.findAll(pageSize, lastItemId);
        } catch (RepositoryException e) {
            return new Fail<>(PROBLEM_WITH_DB_MESSAGE, SERVER_ERROR_CODE);
        }
        if (!products.isEmpty()) {
            String result = new Gson().toJson(products);
            return new Success<>(result);
        } else return new Fail<>(EMPTY_PRODUCT_LIST_MESSAGE, REQUEST_ERROR_CODE);
    }

    private Result<String> removeProductById(long id) {
        if (isNumberNegativeOrZero(id)) return new Fail<>(String.format(NEGATIVE_ID_MESSAGE, id), REQUEST_ERROR_CODE);
        boolean result;
        try {
            result = repo.remove(id);
        } catch (RepositoryException e) {
            return new Fail<>(PROBLEM_WITH_DB_MESSAGE, SERVER_ERROR_CODE);
        }
        return result
                ? new Success<>("Product has been deleted")
                : new Success<>("Product with such id not found");
    }

    private boolean isNumberNegativeOrZero(long number) {
        return number <= 0;
    }



    private Result<String> handleGetRequest(String data) {
        if (Pattern.matches(PAGING_REGEX, data)) {
            String[] params = data.split("&");
            String lastIndex = params[0].split("=")[1];
            String pageSize = params[1].split("=")[1];
            return findAll(pageSize, lastIndex);
        }else {
            return getById(data.split("=")[1]);
        }
    }
}