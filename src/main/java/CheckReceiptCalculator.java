
import data.model.DiscountCard;
import data.model.Product;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class CheckReceiptCalculator {

    public static List<String> calculateCheckReceipt(Map<Product, Integer> map, DiscountCard card) {
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

    private static String toDollarsWithCents(long costInCents) {
        return "$" + costInCents / 100 + "," + costInCents % 100;
    }
}
