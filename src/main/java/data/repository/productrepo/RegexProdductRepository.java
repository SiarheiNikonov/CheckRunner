package data.repository.productrepo;

import data.model.Product;
import util.exceptions.RepositoryInitializationException;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class RegexProdductRepository {
    public static final String INPUT_DATA_FILE = "src/main/resources/order.txt";
    public static final String INVALID_DATA_FILE = "src/main/resources/invalidData.txt";
    private static RegexProdductRepository instance;
    private Map<Product, Integer> order;

    private RegexProdductRepository() throws IOException {
        order = new HashMap<>();
        File productsFile = new File(INPUT_DATA_FILE);
        File wrongProductsFile = new File(INVALID_DATA_FILE);
        FileReader fr = new FileReader(productsFile);
        BufferedReader reader = new BufferedReader(fr);
        FileWriter fw = new FileWriter(wrongProductsFile);
        BufferedWriter writer = new BufferedWriter(fw);
        String regex =
                "^([1-9][0-9]?|100);([A-Z][a-z]{2,29}|[А-Я][а-я]{2,29});([1-9][0-9]?.\\d{2}|100.00);([1-9]|1\\d|20)$";
        // Так было красиво, но неправильно
        //String regex = "^([1-9][0-9]?|100);\\p{Lu}\\p{Ll}{2,29};([1-9][0-9]?|100).\\d{2};([1-9]|1\\d|20)$";
        while (reader.ready()) {
            String product = reader.readLine();
            if (!Pattern.matches(regex, product)) {
                writer.write(product + "\r\n");
            } else {
                String[] params = product.split(";");
                int id = Integer.parseInt(params[0]);
                String title = params[1];
                int priceInCents = Integer.parseInt(params[2].replace(".", ""));
                int count = Integer.parseInt(params[3]);
                order.put(new Product(id, title, priceInCents), count);
            }
        }
        writer.flush();
        reader.close();
        writer.close();
    }

    public Map<Product, Integer> getOrder() {
        return order;
    }

    public static RegexProdductRepository getInstance() throws RepositoryInitializationException {

        try {
            if (instance == null) instance = new RegexProdductRepository();
        } catch (IOException e) {
            throw new RepositoryInitializationException(e.getMessage());
        }
        return instance;
    }
}
