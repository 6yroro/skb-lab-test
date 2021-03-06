package com.test.skblab.services;

import com.test.skblab.mail.EmailAddress;
import com.test.skblab.mail.EmailContent;
import com.test.skblab.mail.SendMailer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeoutException;

/**
 * @author Alexander Zubkov
 */
@Service
public class MailService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final SendMailer sendMailer;

    @Autowired
    public MailService(SendMailer sendMailer) {
        this.sendMailer = sendMailer;
    }

    @Retryable(value = TimeoutException.class, backoff = @Backoff(delay = 100))
    void sendMailWithRetry(EmailAddress emailAddress, EmailContent emailContent) throws TimeoutException {
        try {
            sendMailer.sendMail(emailAddress, emailContent);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw e;
        }
    }
}
