package ru.clevertec.service.checkreceipt;

import com.itextpdf.text.DocumentException;
import kotlin.Pair;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.clevertec.data.model.CheckReceipt;
import ru.clevertec.data.model.DiscountCard;
import ru.clevertec.data.model.Product;
import ru.clevertec.service.model.state.Fail;
import ru.clevertec.service.model.state.Result;
import ru.clevertec.service.model.state.Success;
import ru.clevertec.data.repository.cardrepo.DiscountCardRepository;
import ru.clevertec.data.repository.productrepo.ProductRepository;
import ru.clevertec.service.checkreceipt.calculator.CheckReceiptCalculator;
import ru.clevertec.service.checkreceipt.writer.CheckReceiptWriter;
import ru.clevertec.util.exceptions.RepositoryException;

import java.io.IOException;
import java.io.OutputStream;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Pattern;

@Service
@AllArgsConstructor
public class CheckReceiptServiceImpl implements CheckReceiptService {

    private final DiscountCardRepository cardRepo;
    private final ProductRepository productRepo;
    private final CheckReceiptCalculator calculator;
    private final CheckReceiptWriter writer;

    @Override
    public Result<Boolean> handleRequest(String query, OutputStream outputStream) {
        if (!isValidQuery(query))
            return new Fail<>("Check query! Must be some like 1=1&3=2&card_id=7", 400);

        AtomicBoolean isRepositoryException = new AtomicBoolean(false);
        List<Integer> notFoundProductsId = new ArrayList<>();
        DiscountCard card = null;
        Map<Product, Integer> order = new HashMap<>();
        String[] params = query.split("&");
        int paramsSize = params.length;
        String lastParam = params[paramsSize - 1];
        if (lastParam.contains("card_id")) {
            try {
                card = getCard(lastParam);
            } catch (RepositoryException e) {
                return new Fail<>("Problem with db has occurred", 500);
            }
            if (card == null) return new Fail<>("Card not found. Try again!", 400);
            paramsSize--;
        }
        Arrays.stream(params, 0, paramsSize).forEach(param -> {
            try {
                Pair<Product, Integer> productWithCountOrNullWithId = mapPositionParam(param);
                Product product = productWithCountOrNullWithId.getFirst();
                if (product == null) notFoundProductsId.add(productWithCountOrNullWithId.getSecond());
                else {
                    Integer currentCount = order.get(productWithCountOrNullWithId.getFirst());
                    Integer count = currentCount == null
                            ? productWithCountOrNullWithId.getSecond()
                            : currentCount + productWithCountOrNullWithId.getSecond();
                    order.put(productWithCountOrNullWithId.getFirst(), count);
                }
            } catch (RepositoryException e) {
                isRepositoryException.set(true);
            }
        });

        if (isRepositoryException.get()) return new Fail<>("Problem with db has occurred", 500);
        if (!notFoundProductsId.isEmpty()) {
            StringBuilder builder = notFoundProductsId.stream().collect(
                    StringBuilder::new,
                    (builder1, id) -> {
                        builder1.append(id);
                        builder1.append(", ");
                    },
                    StringBuilder::append
            );
            return new Fail<>(
                    String.format(
                            "Products with id [%s] not found",
                            builder.substring(0, builder.length() - 2)
                    ),
                    400
            );
        }

        CheckReceipt checkReceipt = calculator.calculateCheckReceipt(order, card);

        try {
            writer.writeCheck(checkReceipt, outputStream);
        } catch (DocumentException|IOException e) {
            e.printStackTrace();
            return new Fail<>("Unable to write response", 500);
        }
        return new Success<>(true);
    }

    private DiscountCard getCard(String cardInfo) throws RepositoryException {

        String cardIdText = cardInfo.split("=")[1];
        int cardId = Integer.parseInt(cardIdText);
        return cardRepo.getById(cardId);
    }

    private Pair<Product, Integer> mapPositionParam(String query) throws RepositoryException {
        String[] params = query.split("=");
        int productId = Integer.parseInt(params[0]);
        int count = Integer.parseInt(params[1]);
        Product product = productRepo.getById(productId);
        if (product == null) return new Pair<>(null, productId);
        return new Pair<>(product, count);
    }


    private boolean isValidQuery(String query) {
        String regex = "^[1-9]\\d*=[1-9]\\d*(&[1-9]\\d*=[1-9]\\d*)*(&card_id=\\d+)?$";
        return Pattern.matches(regex, query);
    }
}
