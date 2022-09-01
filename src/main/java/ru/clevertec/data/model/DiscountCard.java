package ru.clevertec.data.model;

import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
public class DiscountCard {
    Integer id;
    DiscountCardType cardType;
}
