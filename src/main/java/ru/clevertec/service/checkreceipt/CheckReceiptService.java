package ru.clevertec.service.checkreceipt;

import ru.clevertec.data.model.state.Result;

import java.io.OutputStream;

public interface CheckReceiptService {
    Result<Boolean> handleRequest(String query, OutputStream outputStream);
}
