package ru.clevertec.data.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DiscountCardType {
    Integer typeId;
    String typeTitle;
    Integer discount;
}
