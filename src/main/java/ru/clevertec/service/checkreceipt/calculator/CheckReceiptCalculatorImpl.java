package ru.clevertec.service.checkreceipt.calculator;

import kotlin.Pair;
import org.springframework.stereotype.Component;
import ru.clevertec.data.model.CheckReceipt;
import ru.clevertec.data.model.CheckReceiptItem;
import ru.clevertec.data.model.DiscountCard;
import ru.clevertec.data.model.Product;

import java.util.*;

@Component
public class CheckReceiptCalculatorImpl implements CheckReceiptCalculator {
    @Override
    public CheckReceipt calculateCheckReceipt(Map<Product, Long> order, DiscountCard card) {
        List<String> header = new LinkedList<>();
        header.add("CASH RECEIPT");
        header.add("SOME USELESS INFO");

        List<CheckReceiptItem> items = new ArrayList<>(order.size());
        List<String> rows = new LinkedList<>();
        long total = 0;
        long wholesaleDiscount = 0;
        long cardDiscount = 0;
        long discount;

        for (Map.Entry<Product, Long> entry : order.entrySet()) {
            discount = 0;
            Product product = entry.getKey();
            long price = product.getPriceInCents();
            long fullCost = price * entry.getValue();
            total += fullCost;

            if (product.isOnSale() && entry.getValue() > 5) {
                discount = (int) Math.round(fullCost * 0.1);
                wholesaleDiscount += discount;
            }

            CheckReceiptItem item = new CheckReceiptItem(
                    entry.getValue(),
                    product,
                    product.getPriceInCents(),
                    fullCost,
                    discount > 0? discount: 0);
            items.add(item);
        }

        List<Pair<String, Long>> footer = new ArrayList<>(4);
        footer.add(new Pair<>("SUMMARY COST: ", total));
        if (wholesaleDiscount != 0) footer.add(new Pair<>("WHOLESALE DISCOUNT: ", wholesaleDiscount));
        if (card != null) {
            cardDiscount = (int) Math.round((total - wholesaleDiscount) * card.getCardType().getDiscount() / 100.0);
            footer.add(new Pair<>("CARD DISCOUNT: ",cardDiscount));
        }
        footer.add(new Pair<>("TOTAL: ", total - wholesaleDiscount - cardDiscount));

        return new CheckReceipt(header, items, footer);
    }
}
