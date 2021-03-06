package com.test.skblab.services;

import com.test.skblab.database.entities.User;
import com.test.skblab.mail.EmailAddress;
import com.test.skblab.mail.EmailContent;
import com.test.skblab.messaging.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeoutException;

/**
 * @author Alexander Zubkov
 */
@Service
public class MessageProcessingService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final UserService userService;
    private final MailService mailService;

    public MessageProcessingService(UserService userService, MailService mailService) {
        this.userService = userService;
        this.mailService = mailService;
    }

    /*
    обновление данных пользователя и отправка на почту атомарная операция
     */
    @Transactional
    public void process(Message message) throws TimeoutException {
        User user = (User) message.getMessageData();
        userService.updateUser(user);
        mailService.sendMailWithRetry(new EmailAddress(user.getEmail()), getEmailContent(user));
        log.info("User " + user.getLogin() + " is updated");
    }

    private EmailContent getEmailContent(User user) {
        return new EmailContent(
                String.format("User registration for user %s is %s",
                        user.getLogin(),
                        user.getApproval() ? "approve" : "disapprove")
        );
    }

}
