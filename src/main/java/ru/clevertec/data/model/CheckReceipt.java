package ru.clevertec.data.model;

import kotlin.Pair;

import java.util.List;

public class CheckReceipt {
    private final List<String> header;
    private final List<CheckReceiptItem> items;
    private final List<Pair<String, Long>> footer;

    public CheckReceipt(List<String> header, List<CheckReceiptItem> items, List<Pair<String, Long>> footerItems) {
        this.header = header;
        this.items = items;
        this.footer = footerItems;
    }

    public List<String> getHeader() {
        return header;
    }

    public List<CheckReceiptItem> getItems() {
        return items;
    }

    public List<Pair<String, Long>> getFooterItems() {
        return footer;
    }
}
