package ru.clevertec.service.checkreceipt.writer;


import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import kotlin.Pair;
import org.springframework.stereotype.Component;
import ru.clevertec.data.model.CheckReceipt;
import ru.clevertec.data.model.CheckReceiptItem;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;

@Component
public class PdfCheckReceiptWriterImpl implements CheckReceiptWriter {

    private static final String FONT = "fonts/arial.ttf";
    private static final String LOGO = "/img/clevertec_logo.png";

    @Override
    public void writeCheck(CheckReceipt receipt, OutputStream outputStream) throws DocumentException, IOException {
        Document document = new Document();

        document.setPageSize(new Rectangle(600, 600));
        PdfWriter wr = PdfWriter.getInstance(document, outputStream);
        document.open();
        BaseFont baseFont = BaseFont.createFont(FONT, BaseFont.IDENTITY_H, true);
        Font font = new Font(baseFont, 10f);
        PdfPTable headerTable = getHeaderTable(receipt.getHeader(), font);
        PdfPTable bodyTable = getBodyTable(receipt.getItems(), font);
        PdfPTable footerTable = getFooterTable(receipt.getFooterItems(), font);

        document.add(headerTable);
        document.add(bodyTable);
        document.add(footerTable);

        PdfContentByte canvas = wr.getDirectContentUnder();

        Image image = Image.getInstance(getClass().getClassLoader().getResource(LOGO));
        image.scaleAbsolute(document.getPageSize());
        image.setAbsolutePosition(0, 0);
        canvas.addImage(image);
        document.close();
    }

    private void addCellToTable(Phrase phrase, PdfPCell cell, PdfPTable table) {
        cell.setPhrase(phrase);
        table.addCell(cell);
    }

    private String toDollarsWithCents(long costInCents) {
        if (costInCents == 0) return " ";
        return String.format("$%.2f", (costInCents / 100f));
    }

    private PdfPTable getHeaderTable(List<String> header, Font font) {
        PdfPTable headerTable = new PdfPTable(1);
        PdfPCell cell = new PdfPCell();
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBorder(0);
        header.forEach(row -> addCellToTable(new Phrase(row, font), cell, headerTable));
        return headerTable;
    }

    private PdfPTable getBodyTable(List<CheckReceiptItem> items, Font font) throws DocumentException {
        PdfPTable bodyTable = new PdfPTable(5);
        bodyTable.setWidths(new int[]{2, 5, 2, 2, 2});
        PdfPCell cell = new PdfPCell();
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setBorder(0);
        cell.setBorderWidthTop(1);
        cell.setBorderWidthBottom(1);
        Arrays.stream(new String[]{"qty", "title", "one", "all", "sale"}).forEach(item ->
                addCellToTable(new Phrase(item, font), cell, bodyTable)
        );

        cell.setBorder(0);
        for (CheckReceiptItem position : items) {
            addCellToTable(new Phrase(String.valueOf(position.getQuantity()), font), cell, bodyTable);
            addCellToTable(new Phrase(position.getProduct().getTitle(), font), cell, bodyTable);
            addCellToTable(new Phrase(toDollarsWithCents(position.getPricePerOneInCents()), font), cell, bodyTable);
            addCellToTable(new Phrase(toDollarsWithCents(position.getFullPriceInCents()), font), cell, bodyTable);
            addCellToTable(new Phrase(toDollarsWithCents(position.getDiscountInCents()), font), cell, bodyTable);
        }
        return bodyTable;
    }

    private PdfPTable getFooterTable(List<Pair<String, Long>> rows, Font font) throws DocumentException {
        PdfPTable footerTable = new PdfPTable(2);
        footerTable.setWidths(new int[]{9, 4});
        PdfPCell cell = new PdfPCell();
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setBorder(0);
        rows.forEach(row -> {
                    addCellToTable(new Phrase(row.getFirst(), font), cell, footerTable);
                    addCellToTable(new Phrase(toDollarsWithCents(row.getSecond()), font), cell, footerTable);
                }
        );
        return footerTable;
    }

}
