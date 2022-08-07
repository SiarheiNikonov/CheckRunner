package ru.clevertec.data.model;

import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
public class Company {
    Integer companyId;
    String companyName;
    String companyAddress;
    String companyTelNumber;
}
