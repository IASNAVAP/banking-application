package com.bank.banking_application.repo;

import com.bank.banking_application.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepo extends JpaRepository<Transaction,String> {
    List<Transaction> findByAccountNumber(String accountnumber);

}
