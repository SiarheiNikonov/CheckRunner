package ru.clevertec.data.model;

import kotlin.Pair;
import lombok.AllArgsConstructor;
import lombok.Value;

import java.util.List;

@Value
@AllArgsConstructor
public class CheckReceipt {

    List<String> header;
    List<CheckReceiptItem> items;
    List<Pair<String, Long>> footerItems;

}
