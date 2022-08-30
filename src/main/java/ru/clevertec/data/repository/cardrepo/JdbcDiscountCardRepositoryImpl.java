package ru.clevertec.data.repository.cardrepo;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.clevertec.data.model.DiscountCard;
import ru.clevertec.data.model.DiscountCardType;
import ru.clevertec.util.exceptions.RepositoryException;
import ru.clevertec.util.jdbc.ConnectionPool;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
@AllArgsConstructor
public class JdbcDiscountCardRepositoryImpl implements DiscountCardRepository {
    private final ConnectionPool pool;

    private static final int DEFAULT_PAGE_SIZE = 20;

    private static final String GET_CARD_BY_ID_QUERY =
            "SELECT card_id, discount_card_types.type_title, discount_card_types.type_id, discount_card_types.discount_percent FROM discount_cards  " +
                    "JOIN discount_card_types ON discount_cards.card_type = discount_card_types.type_id " +
                    "WHERE card_id =?";

    private static final String GET_DISCOUNT_CARD_TYPE_ID_BY_TITLE_QUERY =
            "SELECT type_id FROM discount_card_types WHERE type_title = ?";
    private static final String ADD_CARD_QUERY =
            "INSERT INTO discount_cards (card_type) VALUES (?)";

    private static final String REMOVE_CARD_BY_ID_QUERY =
            "DELETE FROM discount_cards WHERE card_id = ?";

    private static final String UPDATE_CARD_TYPE_BY_ID_QUERY =
            "UPDATE discount_cards " +
                    "SET card_type = " +
                    "(SELECT type_id FROM discount_card_types WHERE type_title = ?)" +
                    "WHERE card_id = ?";

    private static final String FIND_ALL_WITH_PAGING_QUERY =
            "SELECT discount_cards.card_id, discount_card_types.type_title, discount_card_types.type_id, discount_card_types.discount_percent " +
                    "FROM discount_cards " +
                    "JOIN discount_card_types ON discount_card_types.type_id = discount_cards.card_type " +
                    "WHERE card_id > ? " +
                    "ORDER BY discount_cards.card_id ASC " +
                    "LIMIT ?";

    @Override
    public List<DiscountCard> findAll(Integer pageSize, long lastCardId) throws RepositoryException {
        try (Connection conn = pool.getConnection()) {
            PreparedStatement statement = conn.prepareStatement(FIND_ALL_WITH_PAGING_QUERY);
            statement.setLong(1, lastCardId);
            statement.setInt(2, pageSize == null ? DEFAULT_PAGE_SIZE : pageSize);
            ResultSet resultSet = statement.executeQuery();
            List<DiscountCard> cards = new ArrayList<>(pageSize == null ? DEFAULT_PAGE_SIZE : pageSize);
            while (resultSet.next()) {
                DiscountCardType type = new DiscountCardType(
                        resultSet.getInt(3),
                        resultSet.getString(2),
                        resultSet.getInt(4)
                );
                cards.add(new DiscountCard(resultSet.getInt(1),type ));
            }
            statement.close();
            return cards;
        } catch (SQLException e) {
            throw new RepositoryException(e, "Something went wrong");
        }
    }

    public boolean remove(Integer id) throws RepositoryException {
        try (Connection conn = pool.getConnection()) {
            PreparedStatement statement = conn.prepareStatement(REMOVE_CARD_BY_ID_QUERY);
            statement.setInt(1, id);
            int changedRows = statement.executeUpdate();
            statement.close();
            return changedRows == 1;
        } catch (SQLException e) {
            throw new RepositoryException(e, "Something went wrong");
        }
    }

    @Override
    public boolean update(DiscountCard product) throws RepositoryException {
        try (Connection conn = pool.getConnection()) {
            PreparedStatement statement = conn.prepareStatement(UPDATE_CARD_TYPE_BY_ID_QUERY);
            statement.setInt(2, product.getId());
            statement.setString(1, product.getCardType().getTypeTitle());
            int changedRows = statement.executeUpdate();
            statement.close();
            return changedRows == 1;
        } catch (SQLException e) {
            throw new RepositoryException(e, "Something went wrong");
        }
    }

    @Override
    public DiscountCard add(DiscountCard card) throws RepositoryException {
        try (Connection conn = pool.getConnection()) {
            PreparedStatement statement = conn.prepareStatement(GET_DISCOUNT_CARD_TYPE_ID_BY_TITLE_QUERY);
            statement.setString(1, card.getCardType().getTypeTitle());
            ResultSet resultSet = statement.executeQuery();
            int typeId;
            if (resultSet.next()) {
                typeId = resultSet.getInt("type_id");
            } else return null;
            statement.close();
            statement = conn.prepareStatement(ADD_CARD_QUERY, Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1, typeId);
            statement.executeUpdate();
            resultSet = statement.getGeneratedKeys();
            if(resultSet.next()) {
                int id = resultSet.getInt("card_id");
                statement.close();
                return new DiscountCard(id, card.getCardType());
            } else return null;
        } catch (SQLException e) {
            throw new RepositoryException(e, "Something went wrong");
        }
    }

    @Override
    public DiscountCard getById(int id) throws RepositoryException {
        try (Connection conn = pool.getConnection()) {
            PreparedStatement statement = conn.prepareStatement(GET_CARD_BY_ID_QUERY);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            DiscountCard discountCard = null;
            if (resultSet.next()) {
                String cardTypeTitle = resultSet.getString(2);
                DiscountCardType cardType = new DiscountCardType(
                        resultSet.getInt(3),
                        resultSet.getString(2),
                        resultSet.getInt(4)
                );
                int cardId = resultSet.getInt(1);
                discountCard = new DiscountCard(cardId, cardType);
                statement.close();
            }
            return discountCard;
        } catch (SQLException e) {
            throw new RepositoryException(e, "Something went wrong");
        }
    }
}
