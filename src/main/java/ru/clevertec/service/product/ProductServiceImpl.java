package ru.clevertec.service.product;

import ru.clevertec.data.model.Product;
import ru.clevertec.data.model.state.Fail;
import ru.clevertec.data.model.state.Result;
import ru.clevertec.data.model.state.Success;
import ru.clevertec.data.repository.CrudRepository;
import ru.clevertec.data.repository.productrepo.ProductRepository;
import ru.clevertec.util.exceptions.RepositoryException;

import java.util.List;

public class ProductServiceImpl implements ProductService {

    private static final String PROBLEM_WITH_DB_MESSAGE = "Problem with db has occurred";
    private static final String NEGATIVE_ID_MESSAGE = "Product id = %d is not valid (negative or zero)";
    private static final String WRONG_ID_FORMAT_MESSAGE = "Wrong id format (%s)";
    private static final String PRODUCT_WITH_ID_NOT_FOUND_MESSAGE = "Product with id = %d not found";
    private static final String UNSUCCESSFUL_OPERATION_MESSAGE = "Unsuccessful product %s operation (id = %d)";
    private static final String WRONG_PAGE_OR_LAST_ID_MESSAGE = "Wrong page size or last id format";
    private static final String NEGATIVE_PAGE_OR_LAST_ID_MESSAGE = "%s = %d is not valid (negative or zero)";
    private static final String EMPTY_PRODUCT_LIST_MESSAGE = "There are no products!";
    private static final int SERVER_ERROR_CODE = 500;
    private static final int REQUEST_ERROR_CODE = 400;

    private final CrudRepository<Product> repo;

    public ProductServiceImpl(ProductRepository repo) {
        this.repo = repo;
    }

    @Override
    public Result<Product> getById(String idText) {
        int id;
        try {
            id = Integer.parseInt(idText);
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
        if (product == null) return new Fail<>(String.format(PRODUCT_WITH_ID_NOT_FOUND_MESSAGE, id), REQUEST_ERROR_CODE);
        return new Success<>(product);
    }

    @Override
    public Result<Boolean> add(Product product) {
        Product result;
        try {
            result = repo.add(product);
        } catch (RepositoryException e) {
            return new Fail<>(PROBLEM_WITH_DB_MESSAGE, SERVER_ERROR_CODE);
        }
        if (result != null) return new Success<>(true);
        else return new Fail<>(String.format(UNSUCCESSFUL_OPERATION_MESSAGE, "adding", product.getId()), REQUEST_ERROR_CODE);
    }

    @Override
    public Result<Boolean> removeById(String idText) {
        int id;
        try {
            id = Integer.parseInt(idText);
        } catch (NumberFormatException e) {
            return new Fail<>(String.format(WRONG_ID_FORMAT_MESSAGE, idText), REQUEST_ERROR_CODE);
        }
        return removeProductById(id);
    }

    @Override
    public Result<Boolean> update(Product product) {
        boolean result;
        try {
            result = repo.update(product);
        } catch (RepositoryException e) {
            return new Fail<>(PROBLEM_WITH_DB_MESSAGE, SERVER_ERROR_CODE);
        }
        if (result) return new Success<>(true);
        else
            return new Fail<>(String.format(UNSUCCESSFUL_OPERATION_MESSAGE, "updating", product.getId()), REQUEST_ERROR_CODE);
    }

    @Override
    public Result<List<Product>> findAll(String pageSizeText, String lastItemIdText) {
        int pageSize;
        int lastItemId;
        try {
            pageSize = Integer.parseInt(pageSizeText);
            lastItemId = Integer.parseInt(lastItemIdText);
        } catch (NumberFormatException e) {
            return new Fail<>(WRONG_PAGE_OR_LAST_ID_MESSAGE, REQUEST_ERROR_CODE);
        }
        if(isNumberNegativeOrZero(pageSize)) {
            return new Fail<>(String.format(NEGATIVE_PAGE_OR_LAST_ID_MESSAGE, "Page", pageSize), REQUEST_ERROR_CODE);
        }
        if(isNumberNegativeOrZero(lastItemId)) {
            return new Fail<>(String.format(NEGATIVE_PAGE_OR_LAST_ID_MESSAGE, "Last item id", pageSize), REQUEST_ERROR_CODE);
        }
        List<Product> products;
        try {
            products = repo.findAll(pageSize, lastItemId);
        } catch (RepositoryException e) {
            return new Fail<>(PROBLEM_WITH_DB_MESSAGE, SERVER_ERROR_CODE);
        }
        if (!products.isEmpty()) return new Success<>(products);
        else return new Fail<>(EMPTY_PRODUCT_LIST_MESSAGE, REQUEST_ERROR_CODE);
    }

    private Result<Boolean> removeProductById(int id){
        if (isNumberNegativeOrZero(id)) return new Fail<>(String.format(NEGATIVE_ID_MESSAGE, id), REQUEST_ERROR_CODE);
        boolean result;
        try {
            result = repo.remove(id);
        } catch (RepositoryException e) {
            return new Fail<>(PROBLEM_WITH_DB_MESSAGE, SERVER_ERROR_CODE);
        }
        if (result) return new Success<>(true);
        else return new Success<>(false);
    }

    private boolean isNumberNegativeOrZero(int number) {
        return number <= 0;
    }
}
