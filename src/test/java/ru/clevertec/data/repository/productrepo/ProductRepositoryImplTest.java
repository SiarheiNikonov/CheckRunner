package ru.clevertec.data.repository.productrepo;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import ru.clevertec.data.model.Product;
import ru.clevertec.util.exceptions.UnknownProductIdException;

import java.util.stream.Stream;

public class ProductRepositoryImplTest {

    static ProductRepository repo;

    @BeforeAll
    static void initProductRepo(){
        repo = ProductRepositoryImpl.getInstance();
    }

    @ParameterizedTest
    @MethodSource("getCorrectIds")
    void getProductByIdWithCorrectIdsTest(Integer id) {
        Product product = repo.getProductById(id);
        Assertions.assertEquals(id, product.getId());
        Assertions.assertEquals("SAMSUNG", product.getProducer());
    }

    @ParameterizedTest
    @MethodSource("getWrongIds")
    void getProductByIdWithWrongIdsTest(Integer id) {
        Assertions.assertThrows(UnknownProductIdException.class, () ->repo.getProductById(id));
    }

    static Stream<Integer> getCorrectIds(){
        return Stream.of(
                1, 6, 11
        );
    }

    static Stream<Integer> getWrongIds(){
        return Stream.of(0, -1, Integer.MIN_VALUE);
    }

    @AfterAll
    static void destroyRepo(){
        repo = null;
    }
}
