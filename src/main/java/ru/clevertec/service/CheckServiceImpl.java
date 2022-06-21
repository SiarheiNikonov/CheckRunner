package ru.clevertec.service;

import ru.clevertec.data.model.DiscountCard;
import ru.clevertec.data.model.Product;

import java.io.PrintStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class CheckServiceImpl implements CheckService {

    private PrintStream stream;
    public CheckServiceImpl(PrintStream stream) {
        this.stream = stream;
    }

    public void printCheckReceipt(List<String> rows){
        rows.stream().forEach(System.out::println);
        stream.flush();
        stream.close();
    }

    public List<String> calculateCheckReceipt(Map<Product, Integer> map, DiscountCard card) {
        List<String> rows = new LinkedList<>();

        rows.add("CASH RECEIPT");
        rows.add("SOME USELESS INFO");

        long total = 0;
        long wholesaleDiscount = 0;
        long cardDiscount = 0;
        long discount;

        for (Map.Entry<Product, Integer> entry : map.entrySet()) {
            Product product = entry.getKey();
            int price = product.getPriceInCents();
            int fullCost = price * entry.getValue();
            total += fullCost;

            String row = entry.getValue() + " " +
                    product.getTitle() + " " +
                    toDollarsWithCents(product.getPriceInCents()) + " " +
                    toDollarsWithCents(fullCost);

            if (product.isOnSale() && entry.getValue() > 5) {
                discount = Math.round(fullCost * 0.1);
                wholesaleDiscount += discount;
                row += "(-" + toDollarsWithCents(discount) + ")";
            }
            rows.add(row);
        }

        rows.add("SUMMARY COST: " + toDollarsWithCents(total));
        if (wholesaleDiscount != 0) rows.add("WHOLESALE DISCOUNT: " +
                toDollarsWithCents(wholesaleDiscount));

        if (card != null) {
            cardDiscount = Math.round((total - wholesaleDiscount) * card.getCardType().getDiscount() / 100.0);
            rows.add("CARD DISCOUNT: " + toDollarsWithCents(cardDiscount));
        }

        rows.add("TOTAL " + toDollarsWithCents(total - wholesaleDiscount - cardDiscount));

        return rows;
    }

    private String toDollarsWithCents(long costInCents) {
        return String.format("%.2f", (costInCents / 100f));
    }
}
