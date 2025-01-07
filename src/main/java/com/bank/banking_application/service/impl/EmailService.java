package com.bank.banking_application.service.impl;

import com.bank.banking_application.dto.EmailDetails;

public interface EmailService {
    void sendEmailAlert(EmailDetails emailDetails);
    void sendEmailWithAttachment(EmailDetails emailDetails);
}
