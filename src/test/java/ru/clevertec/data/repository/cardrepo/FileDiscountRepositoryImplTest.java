package ru.clevertec.data.repository.cardrepo;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import ru.clevertec.data.model.DiscountCard;
import ru.clevertec.data.model.DiscountCardType;
import ru.clevertec.util.Constants;
import ru.clevertec.util.exceptions.RepositoryInitializationException;

import java.util.stream.Stream;

public class FileDiscountRepositoryImplTest {

    static DiscountCardRepository cardRepo;

    @BeforeAll
    static void initFileCardRepo() throws RepositoryInitializationException {
        cardRepo = FileDiscountCardRepositoryImpl.getInstance(Constants.CARD_FILE_NAME);
    }

    @ParameterizedTest
    @MethodSource("getCorrectIds")
    void getCardById(int id){
        DiscountCard card = cardRepo.getCardById(id);
        Assertions.assertEquals(id, card.getId());
        Assertions.assertEquals(DiscountCardType.WOODEN, card.getCardType());
    }

    static Stream<Integer> getCorrectIds(){
        return Stream.of(1,5,9);
    }

    @AfterAll
    static void destroyRepo(){
        cardRepo = null;
    }
}
