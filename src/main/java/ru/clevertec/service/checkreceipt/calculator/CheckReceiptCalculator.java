package ru.clevertec.service.checkreceipt.calculator;

import ru.clevertec.data.model.CheckReceipt;
import ru.clevertec.data.model.DiscountCard;
import ru.clevertec.data.model.Product;

import java.util.Map;

public interface CheckReceiptCalculator {

    CheckReceipt calculateCheckReceipt(Map<Product, Integer> order, DiscountCard card);

}
