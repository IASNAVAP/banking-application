package com.bank.banking_application.controller;


import com.bank.banking_application.entity.Transaction;
import com.bank.banking_application.service.impl.BankStatement;
import com.itextpdf.text.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileNotFoundException;
import java.util.List;

@RestController
@RequestMapping("/bankstatement")
public class TransactionController {

    @Autowired
    BankStatement bankStatement;

    @GetMapping
    public List<Transaction> generateStatement(@RequestParam String accountNumber,@RequestParam String fromDate,@RequestParam String toDate) throws DocumentException, FileNotFoundException {
        return bankStatement.generateStatement(accountNumber,fromDate,toDate);
    }
    @GetMapping("/getstatement")
    public List<Transaction> getStatement(@RequestParam String accountnumber){
        System.out.println("hello");
        return bankStatement.getStatement(accountnumber);
    }
}
