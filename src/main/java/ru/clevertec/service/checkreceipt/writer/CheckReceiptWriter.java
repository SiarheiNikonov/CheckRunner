package ru.clevertec.service.checkreceipt.writer;

import com.itextpdf.text.DocumentException;
import ru.clevertec.data.model.CheckReceipt;

import java.io.IOException;
import java.io.OutputStream;

public interface CheckReceiptWriter {

    void writeCheck(CheckReceipt receipt, OutputStream outputStream) throws DocumentException, IOException;

}
