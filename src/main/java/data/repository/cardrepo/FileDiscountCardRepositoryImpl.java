package data.repository.cardrepo;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import data.model.DiscountCard;
import util.Constants;
import util.exceptions.CardNotFoundException;
import util.exceptions.RepositoryInitializationException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

public class FileDiscountCardRepositoryImpl implements DiscountCardRepository {

    private List<DiscountCard> cards;
    private static FileDiscountCardRepositoryImpl instance;

    private FileDiscountCardRepositoryImpl() throws RepositoryInitializationException {
        Moshi moshi = (new Moshi.Builder()).build();
        Type type = Types.newParameterizedType(List.class, DiscountCard.class);
        JsonAdapter<List<DiscountCard>> adapter = moshi.adapter(type);
        try (BufferedReader reader = new BufferedReader(new FileReader(Constants.CARD_FILE_NAME))) {
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
        for (DiscountCard card : cards) {
            if (card.getId() == id) return card;
        }
        throw new CardNotFoundException(Constants.UNKNOWN_CARD_MESSAGE);
    }

    public static FileDiscountCardRepositoryImpl getInstance() throws RepositoryInitializationException {
        if (instance == null) instance = new FileDiscountCardRepositoryImpl();
        return instance;
    }
}
