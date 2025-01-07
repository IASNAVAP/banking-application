package com.bank.banking_application.service.impl;

import com.bank.banking_application.dto.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    BankResponse createAccount(UserRequest userRequest);
    BankResponse balanceEnquiry(EnquiryRequest request);
    String nameEnquiry(EnquiryRequest request);
    BankResponse creditAccount(CreditandDebitRequest request);
    BankResponse debitAccount(CreditandDebitRequest request);
    BankResponse transfer(TransferRequest request);
    BankResponse login(LoginDto loginDto);
    AccountInfo userInfo(EnquiryRequest enquiryRequest);
    ResponseEntity<String> updateUser(UpdateUserDto updateUserDto);
}
