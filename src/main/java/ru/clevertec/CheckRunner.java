package ru.clevertec;

import ru.clevertec.data.model.DiscountCard;
import ru.clevertec.data.model.Product;
import ru.clevertec.data.repository.cardrepo.DiscountCardRepository;
import ru.clevertec.data.repository.cardrepo.FileDiscountCardRepositoryImpl;
import ru.clevertec.data.repository.productrepo.FileProductRepositoryImpl;
import ru.clevertec.data.repository.productrepo.ProductRepository;
import ru.clevertec.service.CheckService;
import ru.clevertec.service.CheckServiceImpl;
import ru.clevertec.service.handler.CheckServiceHandler;
import ru.clevertec.util.Constants;
import ru.clevertec.util.exceptions.RepositoryInitializationException;

import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.Proxy;
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
    private CheckService service;

    CheckRunner() {

        try {
            cardRepo = FileDiscountCardRepositoryImpl.getInstance(Constants.CARD_FILE_NAME);
            prodRepo = FileProductRepositoryImpl.getInstance(Constants.PRODUCT_FILE_NAME);
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
        service = new CheckServiceImpl(stream);
        ClassLoader loader = service.getClass().getClassLoader();
        Class[] interfaces = service.getClass().getInterfaces();
        service = (CheckService) Proxy.newProxyInstance(loader, interfaces, new CheckServiceHandler(service));
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

        List<String> rows = checkRunner.service.calculateCheckReceipt(checkRunner.map, checkRunner.card);
        checkRunner.service.printCheckReceipt(rows);
    }
}
