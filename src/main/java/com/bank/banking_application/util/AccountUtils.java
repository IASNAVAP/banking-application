package com.bank.banking_application.util;

import java.time.Year;

public class AccountUtils {

    public static final String ACCOUNT_EXISTS_CODE="001";
    public static final String ACCOUNT_EXISTS_MESSAGE="This User already have account created";
    public static final String ACCOUNT_CREATION_CODE="002";
    public static final String ACCOUNT_CREATION_MESSAGE="Account is successfully created";
    public static final String ACCOUNT_NOT_EXIST_CODE="003";
    public static final String ACCOUNT_NOT_EXIST_MESSAGE="User with Provided details doesn't found";
    public static final String ACCOUNT_FOUND_CODE="004";
    public static final String ACCOUNT_FOUND_SUCCESS="Account found successfully";
    public static final String ACCOUNT_CREDITED_CODE="005";
    public static final String ACCOUNT_CREDITED_SUCCESS="Amount is credited successfully into your account";
    public static final String INSUFFICIENT_BALANCE_CODE="006";
    public static final String INSUFFICIENT_BALANCE_MESSAGE="Insufficient founds in account";
    public static final String ACCOUNT_DEBITED_CODE="007";
    public static final String ACCOUNT_DEBITED_SUCCESS="Your account is successfully debited";
    public static final String TRANSFER_SUCCESSFUL_CODE="008";
    public static final String TRANSFER_SUCCESSFUL_MESSAGE="Transfer Successful";



    public static String generateAccountNumber(){
        Year currentYear = Year.now();
        String year = String.valueOf(currentYear);

        // Generate a random 6-digit number
        int min = 100000;
        int max = 999999;
        int randNumber = (int) (Math.random() * (max - min + 1) + min);
        String randomNumber = String.valueOf(randNumber);

        // Construct the account number by appending year and random number
        StringBuilder accountNumber = new StringBuilder();
        accountNumber.append(year).append(randomNumber);

        return accountNumber.toString();
    }

}
