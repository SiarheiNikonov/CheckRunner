import java.util.LinkedHashMap;
import java.util.Map;

public class CheckRunner {
    private Map<Product, Integer> map = new LinkedHashMap();
    private DiscountCard card;
    DiscountCardRepository cardRepo = new DiscountCardRepositoryImpl();
    ProductRepository prodRepo = ProductRepositoryImpl.getInstance();

    public static void main(String[] args) {
        CheckRunner checkRunner = new CheckRunner();
        for (String arg : args) {
            String[] data = arg.split("-");
            if (data[0].equals("card")) {
                checkRunner.card = checkRunner.cardRepo.getCardById(Integer.parseInt(data[1]));
            } else {
                checkRunner.map.put(checkRunner.prodRepo.getProductById(Integer.parseInt(data[0])), Integer.parseInt(data[1]));
            }
        }

        System.out.println("CASH RECEIPT");
        System.out.println("SOME USELESS INFO");

        long total = 0;
        long wholesaleDiscount = 0;
        long cardDiscount = 0;
        long discount;

        for (Map.Entry<Product, Integer> entry : checkRunner.map.entrySet()) {
            Product product = entry.getKey();
            int price = product.getPriceInCents();
            int fullCost = price * entry.getValue();
            total += fullCost;

            System.out.print(
                    entry.getValue() + " " +
                            product.getTitle() + " " +
                            checkRunner.toDollarsWithCents(product.getPriceInCents()) + " " +
                            checkRunner.toDollarsWithCents(fullCost)
            );

            if (product.isOnSale() && entry.getValue() > 5) {
                discount = Math.round(fullCost * 0.1);
                wholesaleDiscount += discount;
                System.out.println("(-" + checkRunner.toDollarsWithCents(discount) + ")");
            } else System.out.println();
        }

        System.out.println("SUMMARY COST: " + checkRunner.toDollarsWithCents(total));
        if (wholesaleDiscount != 0) System.out.println("WHOLESALE DISCOUNT: " +
                checkRunner.toDollarsWithCents(wholesaleDiscount));

        if (checkRunner.card != null) {
            cardDiscount = Math.round((total - wholesaleDiscount) * checkRunner.card.getCardType().getDiscount() / 100.0);
            System.out.println("CARD DISCOUNT: " + checkRunner.toDollarsWithCents(cardDiscount));
        }

        System.out.println("TOTAL " + checkRunner.toDollarsWithCents(total - wholesaleDiscount - cardDiscount));
    }

    private String toDollarsWithCents(long costInCents) {
        return "$" + costInCents / 100 + "," + costInCents % 100;
    }
}
