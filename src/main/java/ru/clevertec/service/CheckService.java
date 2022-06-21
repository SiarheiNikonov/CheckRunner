package ru.clevertec.service;

import ru.clevertec.data.model.DiscountCard;
import ru.clevertec.data.model.Product;

import java.util.List;
import java.util.Map;

public interface CheckService {

    void printCheckReceipt(List<String> rows);

    List<String> calculateCheckReceipt(Map<Product, Integer> map, DiscountCard card);

}
