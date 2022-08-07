package ru.clevertec.data.repository.cardrepo;

import ru.clevertec.data.model.DiscountCard;
import ru.clevertec.data.model.DiscountCardType;
import ru.clevertec.util.exceptions.RepositoryException;
import ru.clevertec.util.jdbc.ConnectionPool;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JdbcDiscountCardRepositoryImpl implements DiscountCardRepository {
    private final ConnectionPool pool;

    private static final int DEFAULT_PAGE_SIZE = 20;
    private static volatile JdbcDiscountCardRepositoryImpl instance;

    private static final String GET_CARD_BY_ID_QUERY =
            "SELECT card_id, type_title FROM discount_cards  " +
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
            "SELECT discount_cards.card_id, discount_card_types.type_title " +
                    "FROM discount_cards " +
                    "JOIN discount_card_types ON discount_card_types.type_id = discount_cards.card_type " +
                    "WHERE card_id > ? " +
                    "ORDER BY discount_cards.card_id ASC " +
                    "LIMIT ?";

    private JdbcDiscountCardRepositoryImpl(ConnectionPool connectionPool) {
        pool = connectionPool;
    }


    // Можно было перегрузить метод с параметром pageSize и без
    // Можно было вторым параметром передавать не индекс последней карты, а номер страницы,
    // а в запросе ставить OFFSET = pageSize * (pageNumber - 1), но...
    // Нагуглил, что такой способ на больших данных очень медленный.
    //
    // SELECT * FROM messages ORDER BY id ASC LIMIT 100 OFFSET 100000;
    // Limit  (cost=3432.12..3435.56 rows=100 width=37) (actual time=1377.502..1379.512 rows=100 loops=1)
    //   ->  Index Scan using messages_pkey on messages  (cost=0.42..34317.43 rows=1000000 width=37) (actual time=0.046..712.494 rows=100100 loops=1)
    // Planning Time: 0.127 ms
    // Execution Time: 1380.311 ms
    //
    //SELECT * FROM messages WHERE id > 100000 ORDER BY id ASC LIMIT 100;
    // Limit  (cost=0.42..4.11 rows=100 width=37) (actual time=0.090..2.790 rows=100 loops=1)
    //   ->  Index Scan using messages_pkey on messages  (cost=0.42..33203.93 rows=901743 width=37) (actual time=0.076..1.085 rows=100 loops=1)
    //         Index Cond: (id > 100000)
    // Planning Time: 0.076 ms
    // Execution Time: 3.723 ms
    //
    // Разница в 370 раз.
    // И не надо на меня ругаться за комменты в коде :)
    @Override
    public List<DiscountCard> findAll(Integer pageSize, long lastCardId) throws RepositoryException {
        try (Connection conn = pool.getConnection()) {
            PreparedStatement statement = conn.prepareStatement(FIND_ALL_WITH_PAGING_QUERY);
            statement.setLong(1, lastCardId);
            statement.setInt(2, pageSize == null ? DEFAULT_PAGE_SIZE : pageSize);
            ResultSet resultSet = statement.executeQuery();
            List<DiscountCard> cards = new ArrayList<>(pageSize == null ? DEFAULT_PAGE_SIZE : pageSize);
            while (resultSet.next()) {
                cards.add(new DiscountCard(resultSet.getInt(1), DiscountCardType.valueOf(resultSet.getString(2))));
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
            statement.setString(1, product.getCardType().name());
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
            statement.setString(1, card.getCardType().name());
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
            ResultSet result = statement.executeQuery();
            DiscountCard discountCard = null;
            if (result.next()) {
                String cardTypeTitle = result.getString(2);
                DiscountCardType cardType = DiscountCardType.valueOf(cardTypeTitle);
                int cardId = result.getInt(1);
                discountCard = new DiscountCard(cardId, cardType);
                statement.close();
            }
            return discountCard;
        } catch (SQLException e) {
            throw new RepositoryException(e, "Something went wrong");
        }
    }

    public static JdbcDiscountCardRepositoryImpl getInstance(ConnectionPool pool) {
        if (instance == null)
            synchronized (JdbcDiscountCardRepositoryImpl.class) {
                if (instance == null)
                    instance = new JdbcDiscountCardRepositoryImpl(pool);
            }
        return instance;
    }
}
