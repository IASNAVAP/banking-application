package com.bank.banking_application.service.impl;

import com.bank.banking_application.dto.EmailDetails;
import com.bank.banking_application.entity.Transaction;
import com.bank.banking_application.entity.User;
import com.bank.banking_application.repo.TransactionRepo;
import com.bank.banking_application.repo.UserRepo;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.Phaser;

@Component
@AllArgsConstructor
@Slf4j
public class BankStatement {
    private TransactionRepo transactionRepo;
    private UserRepo userRepo;
    private EmailService emailService;
    private static final String FILE="C:\\Users\\vyama\\OneDrive\\Desktop\\Spring Statements\\MY_STATEMENT.pdf";

    public List<Transaction> getStatement(String accountnumber){
        List<Transaction> transcationlist=transactionRepo.findByAccountNumber(accountnumber);
        return transcationlist;
    }

    public List<Transaction> generateStatement(String accountnumber,String startdate,String enddate ) throws FileNotFoundException, DocumentException {
        LocalDate start = LocalDate.parse(startdate, DateTimeFormatter.ISO_DATE);
        LocalDate end = LocalDate.parse(enddate,DateTimeFormatter.ISO_DATE);
        List<Transaction> transactionList = transactionRepo.findAll().stream().filter(transaction -> transaction.getAccountNumber().equals(accountnumber))
                .filter(transaction -> transaction.getCreatedAt().isEqual(start)).filter(transaction -> transaction.getCreatedAt().isEqual(end)).toList();


        User user = userRepo.findByAccountNumber(accountnumber);
        String customerName = user.getFirstName()+" "+user.getLastName();



        Rectangle statementSize=new Rectangle(PageSize.A4);
        Document document=new Document(statementSize);
        log.info("setting size of document");
        OutputStream outputStream = new FileOutputStream(FILE);
        PdfWriter.getInstance(document,outputStream);
        document.open();

        PdfPTable bankInfoTable= new PdfPTable(1);
        PdfPCell bankname = new PdfPCell(new Phrase("PAVAN'S BANK"));
        bankname.setBorder(0);
        bankname.setBackgroundColor(BaseColor.GREEN);
        bankname.setPadding(20f);

        PdfPCell bankAddress = new PdfPCell(new Phrase("77-A, xyz Street, India"));
        bankAddress.setBorder(0);

        bankInfoTable.addCell(bankname);
        bankInfoTable.addCell(bankAddress);

        PdfPTable statementInfo = new PdfPTable(2);
        PdfPCell customerInfo = new PdfPCell(new Phrase("Start Date: "+ startdate) );
        customerInfo.setBorder(0);

        PdfPCell statement = new PdfPCell(new Phrase("STATEMENT OF THE ACCOUNT"));
        statement.setBorder(0);

        PdfPCell stopdate = new PdfPCell(new Phrase("End Date: "+ enddate));
        stopdate.setBorder(0);

        PdfPCell name = new PdfPCell(new Phrase("Customer Name:"+ customerName));
        name.setBorder(0);

        PdfPCell space = new PdfPCell();

        PdfPCell address = new PdfPCell(new Phrase("Customer Address : "+ user.getAddress()));
        address.setBorder(0);

        PdfPTable transactionTable = new PdfPTable(4);

        PdfPCell date = new PdfPCell(new Phrase("DATE"));
        date.setBackgroundColor(BaseColor.GREEN);
        date.setBorder(0);

        PdfPCell transactiontype = new PdfPCell(new Phrase("TRANSACTION TYPE"));
        transactiontype.setBackgroundColor(BaseColor.GREEN);
        transactiontype.setBorder(0);

        PdfPCell tamount = new PdfPCell(new Phrase("TRANSACTION AMOUNT"));
        tamount.setBackgroundColor(BaseColor.GREEN);
        tamount.setBorder(0);

        PdfPCell status = new PdfPCell(new Phrase("STATUS"));
        status.setBackgroundColor(BaseColor.GREEN);
        status.setBorder(0);

        transactionTable.addCell(date);
        transactionTable.addCell(transactiontype);
        transactionTable.addCell(tamount);
        transactionTable.addCell(status);

        transactionList .forEach(transaction -> {
            transactionTable.addCell(new Phrase(transaction.getCreatedAt().toString()));
            transactionTable.addCell(new Phrase(transaction.getTransactionType()));
            transactionTable.addCell(new Phrase(transaction.getAmount().toString()));
            transactionTable.addCell(new Phrase(transaction.getStatus()));
        });

        statementInfo.addCell(customerInfo);
        statementInfo.addCell(statement);
        statementInfo.addCell(stopdate);
        statementInfo.addCell(name);
        statementInfo.addCell(space);
        statementInfo.addCell(address);

        document.add(bankInfoTable);
        document.add(statementInfo);
        document.add(transactionTable);

        document.close();

        EmailDetails emailDetails= EmailDetails.builder()
                .recipient(user.getEmail())
                .subject("STATEMENT OF ACCOUNT")
                .messageBody("Attachment of your account statement is below !")
                .attachment(FILE)
                .build();

        emailService.sendEmailWithAttachment(emailDetails);



        return transactionList;
    }
}
