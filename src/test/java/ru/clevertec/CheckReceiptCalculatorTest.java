package ru.clevertec;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.clevertec.data.model.DiscountCard;
import ru.clevertec.data.model.DiscountCardType;
import ru.clevertec.data.model.Product;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CheckReceiptCalculatorTest {

    @Test
    void calculateCheckReceiptTest() {
        String title = "TEST PRODUCT";
        int priceInCents = 111;
        int count = 7;
        DiscountCard card = new DiscountCard(0, DiscountCardType.WOODEN);
        long fullPrice = priceInCents * count;
        long discount  = Math.round(fullPrice  * card.getCardType().getDiscount() / 100.0);
        Product product = new Product(
                1,
                title,
                priceInCents
        );
        Map<Product, Integer> productsList = new HashMap<>();
        productsList.put(product, count);

        List<String> actualCheck = CheckReceiptCalculator.calculateCheckReceipt(
                productsList,
                card
        );

        List<String> expectedCheck = new ArrayList<>();
        expectedCheck.add("CASH RECEIPT");
        expectedCheck.add("SOME USELESS INFO");
        String expectedProductLine = String.format(
                "%d %s %.2f %.2f", count, title, priceInCents/100f, fullPrice/100f
        );
        expectedCheck.add(expectedProductLine);
        expectedCheck.add(String.format("SUMMARY COST: %.2f", fullPrice/100f));
        expectedCheck.add(String.format("CARD DISCOUNT: %.2f", discount/100f));
        expectedCheck.add(String.format("TOTAL %.2f", (fullPrice - discount)/100f));

        Assertions.assertIterableEquals(expectedCheck, actualCheck);
    }
}
