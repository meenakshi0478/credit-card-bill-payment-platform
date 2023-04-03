package com.credpay.platform.service;


import com.credpay.platform.dto.BillDto;
import com.credpay.platform.model.CreditCard;
import com.credpay.platform.model.Transaction;
import com.credpay.platform.repository.TransactionRepository;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.lowagie.text.pdf.CMYKColor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
public class PDFGeneratorService {

    @Autowired
    private TransactionRepository transactionRepository;

    public byte[] generatePdf(CreditCard creditCard, BillDto createdBill) throws DocumentException {
        List<Transaction> transactions = transactionRepository.findAllByCreditcardId(creditCard.getId());
        Collections.sort(transactions, Comparator.comparing(Transaction::getDate));
        Document document = new Document(PageSize.A4);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, outputStream);

        document.open();
        Font fontTiltle = FontFactory.getFont(FontFactory.TIMES_ROMAN);
        fontTiltle.setSize(20);
        // Creating a table of the 4 columns
        PdfPTable table = new PdfPTable(4);
        // Setting width of the table, its columns and spacing
        table.setWidths(new int[] {3,3,3,3});
        table.setSpacingBefore(5);
        Paragraph header = new Paragraph("Creditcard Statement", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14));
        header.setAlignment(Element.ALIGN_CENTER);
        document.add(header);
        PdfPCell cell = new PdfPCell(new Paragraph("Header"));

        // Create Table Cells for the table header
        // Setting the background color and padding of the table cell
        cell.setBackgroundColor(BaseColor.BLUE);
        cell.setPadding(5);

        Font font = FontFactory.getFont(FontFactory.TIMES_ROMAN);
        font.setColor(BaseColor.WHITE);

        cell.setPhrase(new Phrase("ID", font));
        table.addCell((cell));
        cell.setPhrase(new Phrase(" Amount", font));
        table.addCell((cell));
        cell.setPhrase(new Phrase("Description", font));
        table.addCell((cell));
        cell.setPhrase(new Phrase("date", font));
        table.addCell((cell));
        // Iterating the list of transaction
        for (Transaction transaction: transactions) {
            table.addCell(String.valueOf(transaction.getId()));
            table.addCell(String.valueOf(transaction.getAmount()));
            table.addCell(transaction.getDescription());
            table.addCell(String.valueOf(transaction.getDate()));
        }

        Font boldFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        document.add(new Paragraph(" "));
        Paragraph newParagraph = new Paragraph("Total Bill Amount : Rs " + createdBill.getBillAmount(),boldFont);
        newParagraph.setIndentationLeft(60);
        document.add(newParagraph);
        document.add(table);

        document.close();
        return outputStream.toByteArray();
    }
}
