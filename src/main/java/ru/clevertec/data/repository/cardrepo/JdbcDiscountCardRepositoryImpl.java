package ru.clevertec.data.repository.cardrepo;

import ru.clevertec.data.model.DiscountCard;
import ru.clevertec.data.model.DiscountCardType;
import ru.clevertec.util.exceptions.CardNotFoundException;
import ru.clevertec.util.exceptions.RepositoryInitializationException;
import ru.clevertec.util.jdbc.ConnectionPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JdbcDiscountCardRepositoryImpl implements DiscountCardRepository {
    private final ConnectionPool pool;

    private static volatile JdbcDiscountCardRepositoryImpl instance;

    private static final String GET_CARD_BY_ID_QUERY =
                    "SELECT card_id, type_title FROM discount_cards  " +
                    "JOIN discount_card_types ON discount_cards.card_type = discount_card_types.type_id " +
                    "WHERE card_id =?";

    private JdbcDiscountCardRepositoryImpl(ConnectionPool connectionPool) {
        pool = connectionPool;
    }

    @Override
    public DiscountCard getCardById(int id) {
        try (Connection conn = pool.getConnection()) {
            PreparedStatement statement = conn.prepareStatement(GET_CARD_BY_ID_QUERY);
            statement.setInt(1, id);
            ResultSet result = statement.executeQuery();
            if (result.next()) {
                String cardTypeTitle = result.getString(2);
                DiscountCardType cardType = DiscountCardType.valueOf(cardTypeTitle);
                int cardId = result.getInt(1);
                DiscountCard discountCard = new DiscountCard(cardId, cardType);
                return discountCard;
            } else throw new CardNotFoundException(String.format("Card with id = %s not found", id));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static JdbcDiscountCardRepositoryImpl getInstance(ConnectionPool pool) throws RepositoryInitializationException {
        if (instance == null)
            synchronized (JdbcDiscountCardRepositoryImpl.class) {
                if (instance == null)
                    instance = new JdbcDiscountCardRepositoryImpl(pool);
            }
        return instance;
    }
}
