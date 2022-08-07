package ru.clevertec.service.card;

import com.google.gson.Gson;
import ru.clevertec.data.model.DiscountCard;
import ru.clevertec.data.model.state.Fail;
import ru.clevertec.data.model.state.Result;
import ru.clevertec.data.model.state.Success;
import ru.clevertec.data.repository.CrudRepository;
import ru.clevertec.data.repository.cardrepo.DiscountCardRepository;
import ru.clevertec.service.RequestMethod;
import ru.clevertec.util.exceptions.RepositoryException;

import java.util.List;
import java.util.regex.Pattern;

public class CardServiceImpl implements CardService {

    private static final String PAGING_REGEX = "^last_index=[1-9]+\\d*&page_size=[1-9]+\\d*$";
    private static final String PROBLEM_WITH_DB_MESSAGE = "Problem with db has occurred";
    private static final String NEGATIVE_ID_MESSAGE = "Card id = %d is not valid (negative or zero)";
    private static final String WRONG_ID_FORMAT_MESSAGE = "Wrong id format (%s)";
    private static final String CARD_WITH_ID_NOT_FOUND_MESSAGE = "Card with id = %d not found";
    private static final String UNSUCCESSFUL_OPERATION_MESSAGE = "Unsuccessful card %s operation (id = %d)";
    private static final String WRONG_PAGE_OR_LAST_ID_MESSAGE = "Wrong page size or last id format";
    private static final String NEGATIVE_PAGE_OR_LAST_ID_MESSAGE = "%s = %d is not valid (negative or zero)";
    private static final String EMPTY_PRODUCT_LIST_MESSAGE = "There are no cards!";
    private static final int SERVER_ERROR_CODE = 500;
    private static final int REQUEST_ERROR_CODE = 400;
    private final CrudRepository<DiscountCard> repo;

    public CardServiceImpl(DiscountCardRepository repo) {
        this.repo = repo;
    }

    @Override
    public Result<String> handleRequest(String data, RequestMethod method) {
        switch (method) {
            case GET: {
                return handleGetRequest(data);
            }
            case PUT: {
                DiscountCard card = new Gson().fromJson(data, DiscountCard.class);
                return update(card);
            }
            case POST: {
                DiscountCard card = new Gson().fromJson(data, DiscountCard.class);
                return add(card);
            }
            case DELETE: {
                return removeById(data);
            }
        }
        return new Fail<>("Unknown method", 400);
    }

    private Result<String> getById(String idText) {
        int id;
        try {
            id = Integer.parseInt(idText);
        } catch (NumberFormatException e) {
            return new Fail<>(String.format(WRONG_ID_FORMAT_MESSAGE, idText), REQUEST_ERROR_CODE);
        }
        if (isNumberNegativeOrZero(id)) return new Fail<>(String.format(NEGATIVE_ID_MESSAGE, id), REQUEST_ERROR_CODE);
        DiscountCard card;
        try {
            card = repo.getById(id);
        } catch (RepositoryException e) {
            return new Fail<>(PROBLEM_WITH_DB_MESSAGE, SERVER_ERROR_CODE);
        }
        if (card == null) return new Fail<>(String.format(CARD_WITH_ID_NOT_FOUND_MESSAGE, id), REQUEST_ERROR_CODE);
        String result = new Gson().toJson(card);
        return new Success<>(result);
    }

    private Result<String> add(DiscountCard card) {
        DiscountCard result;
        try {
            result = repo.add(card);
        } catch (RepositoryException e) {
            return new Fail<>(PROBLEM_WITH_DB_MESSAGE, SERVER_ERROR_CODE);
        }
        if (result != null) return new Success<>("Card has been added");
        else
            return new Fail<>(String.format(UNSUCCESSFUL_OPERATION_MESSAGE, "adding", card.getId()), REQUEST_ERROR_CODE);
    }

    private Result<String> removeById(String idText) {
        int id;
        try {
            id = Integer.parseInt(idText);
        } catch (NumberFormatException e) {
            return new Fail<>(String.format(WRONG_ID_FORMAT_MESSAGE, idText), REQUEST_ERROR_CODE);
        }
        return removeCardById(id);
    }

    private Result<String> update(DiscountCard card) {

        boolean result;
        try {
            result = repo.update(card);
        } catch (RepositoryException e) {
            return new Fail<>(PROBLEM_WITH_DB_MESSAGE, SERVER_ERROR_CODE);
        }
        if (result) return new Success<>("Card has been updated");
        else
            return new Fail<>(String.format(UNSUCCESSFUL_OPERATION_MESSAGE, "updating", card.getId()), REQUEST_ERROR_CODE);
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
        List<DiscountCard> cards;
        try {
            cards = repo.findAll(pageSize, lastItemId);
        } catch (RepositoryException e) {
            return new Fail<>(PROBLEM_WITH_DB_MESSAGE, SERVER_ERROR_CODE);
        }
        if (!cards.isEmpty()) {
            String result = new Gson().toJson(cards);
            return new Success<>(result);
        }
        else return new Fail<>(EMPTY_PRODUCT_LIST_MESSAGE, REQUEST_ERROR_CODE);
    }

    private boolean isNumberNegativeOrZero(int number) {
        return number <= 0;
    }

    private Result<String> removeCardById(int id) {
        if (isNumberNegativeOrZero(id)) return new Fail<>(String.format(NEGATIVE_ID_MESSAGE, id), REQUEST_ERROR_CODE);
        boolean result;
        try {
            result = repo.remove(id);
        } catch (RepositoryException e) {
            return new Fail<>(PROBLEM_WITH_DB_MESSAGE, SERVER_ERROR_CODE);
        }
        return result ? new Success<>("Card has been deleted")
                : new Success<>("Card with such id not found");
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
