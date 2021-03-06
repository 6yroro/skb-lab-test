package com.test.skblab.services;

import com.test.skblab.database.entities.User;
import com.test.skblab.messaging.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Alexander Zubkov
 */
@Service
public class UserApprovalService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final MessagingService messagingService;
    private final MessageService<User> messageService;

    @Autowired
    public UserApprovalService(MessagingService messagingService, MessageService<User> messageService) {
        this.messagingService = messagingService;
        this.messageService = messageService;
    }

    void requestApproval(User user) {
        Message<User> message = new Message<>(user);
        message.setMessageId(messagingService.send(message));
        messageService.addMessage(message);
        log.info("Message " + message.getMessageId() + " is send");
    }

}
