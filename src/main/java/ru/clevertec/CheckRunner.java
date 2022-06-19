package ru.clevertec;

import ru.clevertec.data.model.DiscountCard;
import ru.clevertec.data.model.Product;
import ru.clevertec.data.repository.cardrepo.DiscountCardRepository;
import ru.clevertec.data.repository.cardrepo.FileDiscountCardRepositoryImpl;
import ru.clevertec.data.repository.productrepo.FileProductRepositoryImpl;
import ru.clevertec.data.repository.productrepo.ProductRepository;
import ru.clevertec.util.Constants;
import ru.clevertec.util.exceptions.RepositoryInitializationException;

import java.io.IOException;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CheckRunner {
    private Map<Product, Integer> map = new LinkedHashMap();
    private DiscountCard card;
    private DiscountCardRepository cardRepo;
    private ProductRepository prodRepo;
    private PrintStream stream;
    private CheckReceiptPrinter printer;

    CheckRunner() {

        try {
            cardRepo = FileDiscountCardRepositoryImpl.getInstance();
            prodRepo = FileProductRepositoryImpl.getInstance();
        } catch (RepositoryInitializationException e) {
            System.out.println("Something went wrong.");
            System.out.println(e.getLocalizedMessage());
            System.exit(1);
        }

        try {
            stream = new PrintStream(Constants.RECEIPT_FILE_NAME);

        } catch (IOException e) {
            stream = System.out;
            stream.println("Something went wrong.");
            stream.println(e.getLocalizedMessage());
        }
        printer = new CheckReceiptPrinter(stream);
    }

    public static void main(String[] args) {
        CheckRunner checkRunner = new CheckRunner();
        Arrays.stream(args).forEach(arg -> {
            String[] data = arg.split("-");
            if (data[0].equals("card")) {
                checkRunner.card = checkRunner.cardRepo.getCardById(Integer.parseInt(data[1]));
            } else {
                checkRunner.map.put(checkRunner.prodRepo.getProductById(Integer.parseInt(data[0])), Integer.parseInt(data[1]));
            }
        });

        List<String> rows = CheckReceiptCalculator.calculateCheckReceipt(checkRunner.map, checkRunner.card);
        checkRunner.printer.printCheckReceipt(rows);
    }
}
