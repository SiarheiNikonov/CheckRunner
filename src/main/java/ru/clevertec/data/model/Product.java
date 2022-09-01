package ru.clevertec.data.model;

import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
public class Product {
    Integer id;
    String title;
    int priceInCents;
    String description;
    Company producer;
    long barcode;
    boolean onSale;
}
