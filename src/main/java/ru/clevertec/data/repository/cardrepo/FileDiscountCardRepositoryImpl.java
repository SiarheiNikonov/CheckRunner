package ru.clevertec.data.repository.cardrepo;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import ru.clevertec.data.model.DiscountCard;
import ru.clevertec.util.Constants;
import ru.clevertec.util.exceptions.CardNotFoundException;
import ru.clevertec.util.exceptions.RepositoryInitializationException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

public class FileDiscountCardRepositoryImpl implements DiscountCardRepository {

    private List<DiscountCard> cards;
    private static FileDiscountCardRepositoryImpl instance;

    private FileDiscountCardRepositoryImpl(String fileName) throws RepositoryInitializationException {
        Moshi moshi = (new Moshi.Builder()).build();
        Type type = Types.newParameterizedType(List.class, DiscountCard.class);
        JsonAdapter<List<DiscountCard>> adapter = moshi.adapter(type);
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            if (reader.ready()) {
                String json = reader.readLine();
                cards = adapter.fromJson(json);
            }
        } catch (IOException e) {
            throw new RepositoryInitializationException(e.getMessage());
        }
    }

    @Override
    public DiscountCard getCardById(int id) throws CardNotFoundException {
        //Кривая переделка. В цикле могли найти первое совпадение и вернуть его.
        //В данном случае сначала фильтруются ВСЕ элементы, потом проверяется не попался ли такой.
        return cards.stream().filter(card -> card.getId() == id).findFirst().orElseThrow(() -> new CardNotFoundException(Constants.UNKNOWN_CARD_MESSAGE));
    }

    public static FileDiscountCardRepositoryImpl getInstance(String fileName) throws RepositoryInitializationException {
        if (instance == null) instance = new FileDiscountCardRepositoryImpl(fileName);
        return instance;
    }
}
