package com.bank.banking_application.service.impl;

import com.bank.banking_application.config.JwtTokenProvider;
import com.bank.banking_application.dto.*;
import com.bank.banking_application.entity.Role;
import com.bank.banking_application.entity.User;
import com.bank.banking_application.repo.UserRepo;
import com.bank.banking_application.util.AccountUtils;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserServiceImp implements UserService{

    private UserRepo userRepo;
    private EmailService emailService;
    private TransactionService transactionService;
    private PasswordEncoder passwordEncoder;
    private AuthenticationManager authenticationManager;
    private JwtTokenProvider jwtTokenProvider;





    @Override
    public BankResponse createAccount(UserRequest userRequest) {
        if(userRepo.existsByEmail(userRequest.getEmail())){
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_EXISTS_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_EXISTS_MESSAGE)
                    .accountInfo(null)
                    .build();
        }
        User newUser = User.builder()
                .firstName(userRequest.getFirstName())
                .lastName(userRequest.getLastName())
                .otherName(userRequest.getOtherName())
                .gender(userRequest.getGender())
                .address(userRequest.getAddress())
                .stateOfOrigin(userRequest.getStateOfOrigin())
                .accountNumber(AccountUtils.generateAccountNumber())
                .accountBalance(BigDecimal.ZERO)
                .email(userRequest.getEmail())
                .password(passwordEncoder.encode(userRequest.getPassword()))
                .phoneNumber(userRequest.getPhoneNumber())
                .alternativePhoneNumber(userRequest.getAlternativePhoneNumber())
                .status("ACTIVE")
                .role(Role.valueOf("ROLE_USER"))
                .build();

        User savedUser = userRepo.save(newUser);
        EmailDetails emailDetails = EmailDetails.builder()
                .recipient(savedUser.getEmail())
                .subject("Account Created")
                .messageBody("Congratulations! your  account is successfully created \nyour account details: \nAccount Name: "+" "+savedUser.getFirstName()+" "+savedUser.getLastName()+" "+savedUser.getOtherName()+"\n "+ "Account number: "+" "+savedUser.getAccountNumber())
                .build();
        emailService.sendEmailAlert(emailDetails);
        return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_CREATION_CODE)
                .responseMessage(AccountUtils.ACCOUNT_CREATION_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountBalance(savedUser.getAccountBalance())
                        .accoutNumber(savedUser.getAccountNumber())
                        .accountName(savedUser.getFirstName()+" "+savedUser.getLastName()+" "+savedUser.getOtherName())
                        .build())
                .build();

    }

    @Override
    public BankResponse balanceEnquiry(EnquiryRequest request) {
        boolean isAccountExists =userRepo.existsByAccountNumber(request.getAccountNumber());
        if(!isAccountExists){
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_NOT_EXIST_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE)
                    .accountInfo(null)
                    .build();
        }
        User found = userRepo.findByAccountNumber(request.getAccountNumber());

        return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_FOUND_CODE)
                .responseMessage(AccountUtils.ACCOUNT_FOUND_SUCCESS)
                .accountInfo(AccountInfo.builder()
                        .accountBalance(found.getAccountBalance())
                        .accoutNumber(request.getAccountNumber())
                        .accountName(found.getFirstName()+" "+found.getLastName()+" "+found.getOtherName())
                        .build())
                .build();
    }

    @Override
    public String nameEnquiry(EnquiryRequest request) {
        boolean isAccountExists =userRepo.existsByAccountNumber(request.getAccountNumber());
        if(!isAccountExists){
            return AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE;
        }
        User found = userRepo.findByAccountNumber(request.getAccountNumber());
        return found.getFirstName()+" "+found.getLastName()+" "+found.getOtherName();
    }

    @Override
    public BankResponse creditAccount(CreditandDebitRequest request) {
        boolean isAccountExists =userRepo.existsByAccountNumber(request.getAccountNumber());
        if(!isAccountExists){
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_NOT_EXIST_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE)
                    .accountInfo(null)
                    .build();
        }
        User found = userRepo.findByAccountNumber(request.getAccountNumber());
        found.setAccountBalance(found.getAccountBalance().add(request.getAmount()));
        userRepo.save(found);

        TransactionDto transactionDto =TransactionDto.builder()
                .accountNumber(found.getAccountNumber())
                .amount(request.getAmount())
                .transactionType("CREDIT")
                .build();

        transactionService.saveTransaction(transactionDto);

        return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_CREDITED_CODE)
                .responseMessage(AccountUtils.ACCOUNT_CREDITED_SUCCESS)
                .accountInfo(AccountInfo.builder()
                        .accountBalance(found.getAccountBalance())
                        .accoutNumber(request.getAccountNumber())
                        .accountName(found.getFirstName()+" "+found.getLastName()+" "+found.getOtherName())
                        .build())
                .build();

    }

    @Override
    public BankResponse debitAccount(CreditandDebitRequest request) {
        boolean isAccountExists =userRepo.existsByAccountNumber(request.getAccountNumber());
        if(!isAccountExists){
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_NOT_EXIST_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE)
                    .accountInfo(null)
                    .build();
        }
        User found = userRepo.findByAccountNumber(request.getAccountNumber());
        int avail = found.getAccountBalance().toBigInteger().intValue();
        int req= request.getAmount().toBigInteger().intValue();
        if(avail < req) {
            return BankResponse.builder()
                    .responseCode(AccountUtils.INSUFFICIENT_BALANCE_CODE)
                    .responseCode(AccountUtils.INSUFFICIENT_BALANCE_MESSAGE)
                    .accountInfo(null)
                    .build();

        }
        else {
            found.setAccountBalance(found.getAccountBalance().subtract(request.getAmount()));
            userRepo.save(found);

            TransactionDto transactionDto =TransactionDto.builder()
                    .accountNumber(found.getAccountNumber())
                    .amount(request.getAmount())
                    .transactionType("DEBIT")
                    .build();

            transactionService.saveTransaction(transactionDto);
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_DEBITED_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_DEBITED_SUCCESS)
                    .accountInfo(AccountInfo.builder()
                            .accountBalance(found.getAccountBalance())
                            .accoutNumber(request.getAccountNumber())
                            .accountName(found.getFirstName() + " " + found.getLastName() + " " + found.getOtherName())
                            .build())
                    .build();


        }
    }

    @Override
    public BankResponse transfer(TransferRequest request) {
        boolean isdestinationaccountexist = userRepo.existsByAccountNumber(request.getDestinationAccountNumber());
        if(!isdestinationaccountexist){
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_NOT_EXIST_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE)
                    .accountInfo(null)
                    .build();
        }
        User sourcefound = userRepo.findByAccountNumber(request.getSourceAccountNumber());
        if(request.getAmount().compareTo(sourcefound.getAccountBalance()) > 0){
            return BankResponse.builder()
                    .responseCode(AccountUtils.INSUFFICIENT_BALANCE_CODE)
                    .responseCode(AccountUtils.INSUFFICIENT_BALANCE_MESSAGE)
                    .accountInfo(null)
                    .build();

        }
        sourcefound.setAccountBalance(sourcefound.getAccountBalance().subtract(request.getAmount()));
        userRepo.save(sourcefound);
        TransactionDto transactionDto1 =TransactionDto.builder()
                .accountNumber(sourcefound.getAccountNumber())
                .amount(request.getAmount())
                .transactionType("DEBIT")
                .build();

        transactionService.saveTransaction(transactionDto1);
        EmailDetails debitAlert = EmailDetails.builder()
                .subject("DEBIT ALERT")
                .recipient(sourcefound.getEmail())
                .messageBody("The sum of"+" "+request.getAmount()+" "+"has been debited from your account \n The remaining balance is:"+" "+sourcefound.getAccountBalance())
                .build();
        emailService.sendEmailAlert(debitAlert);

        User destfound = userRepo.findByAccountNumber(request.getDestinationAccountNumber());
        destfound.setAccountBalance(destfound.getAccountBalance().add(request.getAmount()));
        userRepo.save(destfound);
        EmailDetails creditAlert = EmailDetails.builder()
                .subject("CREDIT ALERT")
                .recipient(destfound.getEmail())
                .messageBody("The sum of"+" "+request.getAmount()+" "+"has been credited to your account \n The closing balance is:"+" "+ destfound.getAccountBalance())
                .build();
        emailService.sendEmailAlert(creditAlert);

        TransactionDto transactionDto =TransactionDto.builder()
                .accountNumber(destfound.getAccountNumber())
                .amount(request.getAmount())
                .transactionType("CREDIT")
                .build();

        transactionService.saveTransaction(transactionDto);

        return BankResponse.builder()
                .responseCode(AccountUtils.TRANSFER_SUCCESSFUL_CODE)
                .responseMessage(AccountUtils.TRANSFER_SUCCESSFUL_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountBalance(sourcefound.getAccountBalance())
                        .accoutNumber(sourcefound.getAccountNumber())
                        .accountName(null)
                        .build())
                .build();
    }

    public BankResponse login(LoginDto loginDto){
        Optional<User> user = userRepo.findByEmail(loginDto.getEmail());
        Authentication authentication = null;
        authentication= authenticationManager.authenticate((
                new UsernamePasswordAuthenticationToken(loginDto.getEmail(),loginDto.getPassword())
                ));

        EmailDetails loginAlert = EmailDetails.builder()
                .subject("Your logged in")
                .recipient(loginDto.getEmail())
                .messageBody("you logged into your account. \nIf you did not initiate this request , please contact your bank ")
                .build();

        emailService.sendEmailAlert(loginAlert);

        return BankResponse.builder()
                .responseCode("Login Success")
                .responseMessage(jwtTokenProvider.generateToken(authentication))
                .accountInfo(AccountInfo.builder()
                        .accountBalance(user.get().getAccountBalance())
                        .accoutNumber(user.get().getAccountNumber())
                        .accountName(null)
                        .build())
                .build();
    }

    @Override
    public AccountInfo userInfo(EnquiryRequest enquiryRequest) {
        User found = userRepo.findByAccountNumber(enquiryRequest.getAccountNumber());
        return AccountInfo.builder()
                .accountBalance(found.getAccountBalance())
                .accoutNumber(found.getAccountNumber())
                .accountName(found.getFirstName() + " " + found.getLastName())
                .build();
    }

    @Override
    public ResponseEntity<String> updateUser(UpdateUserDto updateUserDto) {
        User user = userRepo.findByAccountNumber(updateUserDto.getAccountNumber());
        user.setEmail(updateUserDto.getEmail());
        user.setAddress(updateUserDto.getAddress());
        user.setPhoneNumber(updateUserDto.getPhoneNumber());
        user.setAlternativePhoneNumber(updateUserDto.getAlternativePhoneNumber());
        user.setStateOfOrigin(updateUserDto.getStateOfOrigin());
        userRepo.save(user);

        return new ResponseEntity<>("saved successfully", HttpStatus.OK);
    }
}
