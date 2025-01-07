package com.bank.banking_application.service.impl;

import com.bank.banking_application.dto.TransactionDto;
import com.bank.banking_application.entity.Transaction;
import org.springframework.stereotype.Service;

@Service
public interface TransactionService {
    void saveTransaction(TransactionDto transaction);
}
