package ru.clevertec;

import ru.clevertec.data.model.Product;
import ru.clevertec.data.repository.productrepo.RegexProdductRepository;
import ru.clevertec.util.Constants;
import ru.clevertec.util.exceptions.RepositoryInitializationException;

import java.io.IOException;
import java.io.PrintStream;
import java.util.List;
import java.util.Map;

public class RegexCheckRunner {
    private Map<Product, Integer> map;
    private PrintStream stream;
    private CheckReceiptPrinter printer;

    RegexCheckRunner() {

        try {
            stream = new PrintStream(Constants.RECEIPT_FILE_NAME);

        } catch (IOException e) {
            stream = System.out;
            stream.println("Something went wrong.");
            stream.println(e.getLocalizedMessage());
        }
        printer = new CheckReceiptPrinter(stream);
    }

    public static void main(String[] args) throws RepositoryInitializationException {
        RegexProdductRepository repo = RegexProdductRepository.getInstance();
        RegexCheckRunner checkRunner = new RegexCheckRunner();
        checkRunner.map = repo.getOrder();
        List<String> rows = CheckReceiptCalculator.calculateCheckReceipt(checkRunner.map, null);
        checkRunner.printer.printCheckReceipt(rows);
    }
}
