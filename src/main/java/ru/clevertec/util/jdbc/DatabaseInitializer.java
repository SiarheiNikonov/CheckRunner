package ru.clevertec.util.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DatabaseInitializer {
    private static final String CREATE_DATABASE_QUERY = "DROP DATABASE IF EXISTS products;\n" +
            "CREATE DATABASE products WITH ENCODING 'UTF8';\n";

    private static final String FILL_DATABASE_QUERY = "CREATE TABLE company (\n" +
            "company_id SERIAL PRIMARY KEY,\n" +
            "company_name VARCHAR(30),\n" +
            "company_address VARCHAR(50),\n" +
            "company_tel_number VARCHAR(15)\n" +
            ");\n" +

            "CREATE TABLE discount_card_types (\n" +
            "type_id SERIAL PRIMARY KEY,\n" +
            "type_title VARCHAR(10),\n" +
            "discount_percent SMALLINT,\n" +
            "CHECK (discount_percent > 0 AND discount_percent < 100)\n" +
            ");\n" +

            "CREATE TABLE discount_cards (\n" +
            "card_id SERIAL PRIMARY KEY,\n" +
            "card_type INT,\n" +
            "FOREIGN KEY (card_type) REFERENCES discount_card_types (type_id)\n" +
            ");\n" +

            "CREATE TABLE product (\n" +
            "product_id SERIAL PRIMARY KEY,\n" +
            "title VARCHAR(30),\n" +
            "price_in_cent INT,\n" +
            "description VARCHAR(200),\n" +
            "company_id_ref INT,\n" +
            "barcode VARCHAR(12),\n" +
            "on_sale BOOLEAN,\n" +
            "FOREIGN KEY (company_id_ref) REFERENCES company (company_id)\n" +
            ");\n" +

            "INSERT INTO discount_card_types (type_title, discount_percent)\n" +
            "VALUES\n" +
            "('WOODEN', 1),\n" +
            "('SILVER', 3),\n" +
            "('GOLD', 5),\n" +
            "('PLATINUM', 10);\n" +

            "INSERT INTO discount_cards (card_type)\n" +
            "VALUES\n" +
            "(1),\n" +
            "(2),\n" +
            "(3),\n" +
            "(4),\n" +
            "(1),\n" +
            "(2),\n" +
            "(3),\n" +
            "(4),\n" +
            "(1),\n" +
            "(2),\n" +
            "(3),\n" +
            "(4);\n" +

            "INSERT INTO company (company_name, company_address, company_tel_number)\n" +
            "VALUES\n" +
            "('SAMSUNG', 'someaddress', '23526246'),\n" +
            "('TOSHIBA', 'someaddress','765432'),\n" +
            "('HP', 'someaddress','1236'),\n" +
            "('LENOVO', 'someaddress','61237278'),\n" +
            "('APPLE', 'someaddress','2343443434');\n" +

            "INSERT INTO product (title, price_in_cent, description, company_id_ref, barcode, on_sale)\n" +
            "VALUES\n" +
            "('Банан', 123, 'вкусный, зрелый, применять по назначению', 4, '234234234234', false),\n" +
            "('Апэлсын', 323, 'круглый, катится', 1, '111134234234', false),\n" +
            "('Киви', 143, 'большое и лохматое', 2, '234444234234', false),\n" +
            "('Арбуз', 125, 'для полётов с большой высоты', 3, '134234234234', true),\n" +
            "('Пистолет Макарова', 13223, 'для детей от трёх лет', 5, '534234234234', false),\n" +
            "('Хвост бобра', 1263, 'почему бы и нет', 1, '734234234234', false),\n" +
            "('Цветочек аленький', 8123, 'гербарий из цветочка', 2, '244234234234', true),\n" +
            "('random title1', 666, 'random description1', 1, '234234234294', false),\n" +
            "('random title2', 777, 'random description1', 2, '111134234284', false),\n" +
            "('random title3', 18888, 'random description2', 3, '234444274234', false),\n" +
            "('random title4', 4444, 'random description3', 1, '234234234634', false),\n" +
            "('random title5', 325553, 'random description4', 4, '111134235234', true),\n" +
            "('random title6', 146663, 'random description5', 5, '234444334234', false);";


    public static void main(String[] args) throws ClassNotFoundException {
        Class.forName(PropsKt.DRIVER);
        try (Connection conn = DriverManager
                .getConnection(
                        "jdbc:postgresql://127.0.0.1:5432/postgres?characterEncoding=utf8",
                        PropsKt.USER,
                        PropsKt.PASSWORD
                )) {
            PreparedStatement st = conn.prepareStatement(CREATE_DATABASE_QUERY);
            st.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        try (Connection conn = DriverManager
                .getConnection(
                        "jdbc:postgresql://127.0.0.1:5432/products?characterEncoding=utf8",
                        PropsKt.USER,
                        PropsKt.PASSWORD
                )) {
            PreparedStatement st = conn.prepareStatement(FILL_DATABASE_QUERY);
            st.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
