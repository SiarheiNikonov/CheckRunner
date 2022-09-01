package ru.clevertec.data.model;

import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
public class CheckReceiptItem {

    long quantity;
    Product product;
    long pricePerOneInCents;
    long fullPriceInCents;
    long discountInCents;

}
