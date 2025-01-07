package com.bank.banking_application.service.impl;

import com.bank.banking_application.dto.TransactionDto;
import com.bank.banking_application.entity.Transaction;
import com.bank.banking_application.repo.TransactionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionImp implements TransactionService{

    @Autowired
    TransactionRepo transactionRepo;

    @Override
    public void saveTransaction(TransactionDto trans) {
        Transaction  transaction = Transaction.builder()
                .transactionType(trans.getTransactionType())
                .accountNumber(trans.getAccountNumber())
                .amount(trans.getAmount())
                .status("SUCCESS")
                .build();

        transactionRepo.save(transaction);
        System.out.println("Transaction saved successfully");

    }
}
