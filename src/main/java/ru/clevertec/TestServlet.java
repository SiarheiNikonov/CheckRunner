package ru.clevertec;

import ru.clevertec.data.model.DiscountCard;
import ru.clevertec.data.model.Product;
import ru.clevertec.data.repository.cardrepo.FileDiscountCardRepositoryImpl;
import ru.clevertec.data.repository.productrepo.FileProductRepositoryImpl;
import ru.clevertec.util.exceptions.RepositoryInitializationException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public class TestServlet extends HttpServlet {
    private FileDiscountCardRepositoryImpl cardRepo;
    private FileProductRepositoryImpl productRepository;
    private Map<Product, Integer> productMap = new LinkedHashMap<>();
    private DiscountCard card;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter writer = resp.getWriter();

        try {
            cardRepo = FileDiscountCardRepositoryImpl.getInstance();
            productRepository = FileProductRepositoryImpl.getInstance();
            Map<String, String[]> map = req.getParameterMap();
            map.entrySet().stream().forEach( param -> {
                if (param.getKey().equals("card")) {
                card = cardRepo.getCardById(Integer.parseInt(param.getValue()[0]));
            } else {
                Product product = productRepository.getProductById(Integer.parseInt(param.getKey()));
                int qty = Arrays.stream(param.getValue()).map(Integer::parseInt).reduce(0, (a, b)->a+b);
                productMap.put(product, qty);
            }});

            List<String> rows = CheckReceiptCalculator.calculateCheckReceipt(productMap, card);

            rows.stream().forEach(writer::println);

        } catch (RepositoryInitializationException e) {
            writer.println("Something went wrong. Try later.");
        } catch (NumberFormatException e) {
            writer.println("Only digits! Try again.");
        }
        writer.close();
        super.doGet(req, resp);
    }
}
