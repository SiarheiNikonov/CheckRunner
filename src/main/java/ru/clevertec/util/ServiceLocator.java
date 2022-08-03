package ru.clevertec.util;

import ru.clevertec.data.repository.cardrepo.DiscountCardRepository;
import ru.clevertec.data.repository.cardrepo.JdbcDiscountCardRepositoryImpl;
import ru.clevertec.data.repository.companyrepo.CompanyRepo;
import ru.clevertec.data.repository.companyrepo.JdbcCompanyRepoImpl;
import ru.clevertec.data.repository.productrepo.JdbcProductRepositoryImpl;
import ru.clevertec.data.repository.productrepo.ProductRepository;
import ru.clevertec.service.checkreceipt.CheckReceiptService;
import ru.clevertec.service.checkreceipt.CheckReceiptServiceImpl;
import ru.clevertec.service.checkreceipt.calculator.CheckReceiptCalculator;
import ru.clevertec.service.checkreceipt.calculator.CheckReceiptCalculatorImpl;
import ru.clevertec.service.card.CardService;
import ru.clevertec.service.card.CardServiceImpl;
import ru.clevertec.service.product.ProductService;
import ru.clevertec.service.product.ProductServiceImpl;
import ru.clevertec.service.checkreceipt.writer.PdfCheckReceiptWriterImpl;
import ru.clevertec.service.checkreceipt.writer.CheckReceiptWriter;
import ru.clevertec.util.exceptions.RepositoryInitializationException;
import ru.clevertec.util.jdbc.ConnectionPool;

import java.util.HashMap;
import java.util.Map;

public class ServiceLocator {

    private static volatile ServiceLocator instance;

    private final Map<Class, Object> dependencies = new HashMap<>();

    private ServiceLocator() throws RepositoryInitializationException {

        ConnectionPool pool = ConnectionPool.getInstance(5);
        CompanyRepo companyRepo = JdbcCompanyRepoImpl.getInstance(pool);
        ProductRepository productRepo = JdbcProductRepositoryImpl.getInstance(pool, companyRepo);
        DiscountCardRepository cardRepo = JdbcDiscountCardRepositoryImpl.getInstance(pool);
        CheckReceiptCalculator checkReceiptCalculator = new CheckReceiptCalculatorImpl();
        CheckReceiptWriter writer = new PdfCheckReceiptWriterImpl();
        CheckReceiptService checkReceiptService = new CheckReceiptServiceImpl(
                cardRepo,
                productRepo,
                checkReceiptCalculator,
                writer
        );

        dependencies.put(CompanyRepo.class, companyRepo);
        dependencies.put(ProductRepository.class, productRepo);
        dependencies.put(DiscountCardRepository.class, cardRepo);
        dependencies.put(ProductService.class, new ProductServiceImpl(productRepo));
        dependencies.put(CardService.class, new CardServiceImpl(cardRepo));
        dependencies.put(CheckReceiptService.class, checkReceiptService);
    }

    public static ServiceLocator getInstance() throws RepositoryInitializationException {
        if (instance == null) {
            synchronized (ServiceLocator.class) {
                if (instance == null) {
                    instance = new ServiceLocator();
                }
            }
        }
        return instance;
    }

    public Object getDependency(Class clazz) {
        return dependencies.get(clazz);
    }
}
