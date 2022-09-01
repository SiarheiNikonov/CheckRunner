package ru.clevertec.service.checkreceipt;

import ru.clevertec.service.model.state.Result;

import java.io.OutputStream;

public interface CheckReceiptService {
    Result<Boolean> handleRequest(String query, OutputStream outputStream);
}
