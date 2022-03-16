package ru.clevertec;

import java.io.PrintStream;
import java.util.List;

class CheckReceiptPrinter {

    private PrintStream stream;
    CheckReceiptPrinter(PrintStream stream) {
        this.stream = stream;
    }

    public void printCheckReceipt(List<String> rows){
        for(String row : rows) {
            stream.println(row);
        }
        stream.flush();
        stream.close();
    }

}
