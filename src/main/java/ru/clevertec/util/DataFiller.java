package ru.clevertec.util;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import ru.clevertec.data.model.DiscountCard;
import ru.clevertec.data.model.DiscountCardType;
import ru.clevertec.data.model.Product;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class DataFiller {

    public static void main(String[] args) {
        fillProductsFile(Constants.PRODUCT_FILE_NAME);
        fillCardFile(Constants.CARD_FILE_NAME);
    }

    private static void fillProductsFile(String file) {
        List<Product> products = ProductGenerator.getProducts();
        writeDataToFile(file, Product.class, products);
    }

    private static void fillCardFile(String file) {
        List<DiscountCard> cards = new ArrayList<>(50);
        for (int i = 1; i <= 50; i++) {
            DiscountCard card;
            switch (i % 4) {
                case (1):
                    card = new DiscountCard(i, DiscountCardType.WOODEN);
                    break;
                case (2):
                    card = new DiscountCard(i, DiscountCardType.SILVER);
                    break;
                case (3):
                    card = new DiscountCard(i, DiscountCardType.GOLD);
                    break;
                default:
                    card = new DiscountCard(i, DiscountCardType.PLATINUM);
            }
            cards.add(card);
        }
        writeDataToFile(file, DiscountCard.class, cards);
    }

    private static void writeDataToFile(String file, Class clazz, List list) {
        Moshi moshi = (new Moshi.Builder()).build();
        Type type = Types.newParameterizedType(List.class, clazz);
        JsonAdapter adapter = moshi.adapter(type);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(adapter.toJson(list));
            writer.flush();
        } catch (IOException e) {
            System.out.println(e.getLocalizedMessage());
        }
    }
}
