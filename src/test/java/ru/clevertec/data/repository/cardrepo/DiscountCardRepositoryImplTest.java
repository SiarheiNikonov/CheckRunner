package ru.clevertec.data.repository.cardrepo;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import ru.clevertec.data.model.DiscountCard;
import ru.clevertec.data.model.DiscountCardType;
import ru.clevertec.data.repository.cardrepo.DiscountCardRepository;
import ru.clevertec.data.repository.cardrepo.DiscountCardRepositoryImpl;
import ru.clevertec.util.exceptions.UnknownDiscountCardId;
import java.util.Random;
import java.util.stream.Stream;

public class DiscountCardRepositoryImplTest {

    static DiscountCardRepository cardRepo;
    static Random random = new Random();

    @BeforeAll
    static void createRepo() {
        cardRepo = new DiscountCardRepositoryImpl();
    }

    @Test
    void getCardByIdWithCorrectIdEqualsOneTest() {
        int id = 1;
        DiscountCard card = cardRepo.getCardById(id);
        Assertions.assertEquals(card.getCardType(), DiscountCardType.WOODEN);
        Assertions.assertEquals(card.getId(), 1);
    }

    @ParameterizedTest
    @MethodSource("getWrongIds")
    void getCardByIdWithWrongIdTest(int id) {
        Assertions.assertThrows(UnknownDiscountCardId.class, () -> cardRepo.getCardById(id)
        );
    }

    @ParameterizedTest
    @MethodSource("getCorrectIds")
    void getCardByIdWithCorrectIdsTest(int id) {
        DiscountCard card = cardRepo.getCardById(id);
        Assertions.assertNotNull(card);
        Assertions.assertEquals(card.getId(), id);
    }

    private static Stream<Integer> getWrongIds() {
        return Stream.of(
                0,
                -1,
                Integer.MIN_VALUE,
                random.nextInt(Integer.MAX_VALUE-2)*(-1)
        );
    }

    private static Stream<Integer> getCorrectIds() {
        return Stream.of(
                1,
                Integer.MAX_VALUE,
                random.nextInt(Integer.MAX_VALUE-2) + 1
        );
    }

    @AfterAll
    static void deleteRepo(){
        cardRepo = null;
    }

}
