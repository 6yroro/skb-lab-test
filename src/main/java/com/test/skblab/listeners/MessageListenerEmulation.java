package com.test.skblab.listeners;

import com.test.skblab.database.entities.User;
import com.test.skblab.messaging.Message;
import com.test.skblab.services.MessageReceivingService;
import com.test.skblab.services.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * @author Alexander Zubkov
 * эмуляция слушателя получения ответов
 */
@Service
public class MessageListenerEmulation {

    private final MessageService<User> messageService;
    private final MessageReceivingService<User> messageReceivingService;

    @Autowired
    public MessageListenerEmulation(MessageService<User> messageService, MessageReceivingService<User> messageReceivingService) {
        this.messageService = messageService;
        this.messageReceivingService = messageReceivingService;
    }

    // эмуляция handleMessage
    @Scheduled(cron = "0 0/1 * * * *")
    public void getMessages() {
        for (Message<User> message : messageService.getMessages().values()) {
            messageReceivingService.receive(message);
        }
    }

}
