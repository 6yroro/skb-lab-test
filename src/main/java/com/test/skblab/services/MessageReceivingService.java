package com.test.skblab.services;

import com.test.skblab.database.entities.User;
import com.test.skblab.messaging.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * @author Alexander Zubkov
 */
@Service
public class MessageReceivingService<T> {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final MessageProcessingService messageProcessingService;
    private final MessageService<T> messageService;
    private final MessagingService messagingService;
    private final RandomService randomService;

    @Autowired
    public MessageReceivingService(MessageProcessingService messageProcessingService, MessageService<T> messageService, MessagingService messagingService, RandomService randomService) {
        this.messageProcessingService = messageProcessingService;
        this.messageService = messageService;
        this.messagingService = messagingService;
        this.randomService = randomService;
    }

    @Async
    public void receive(Message<T> message) {
        try {
            // эмуляция начала обработки
            messageService.removeMessage(message);
            log.info("Message " + message.getMessageId() + " receiving");
            messagingService.receive(message.getMessageId());
            ((User) message.getMessageData()).setApproval(randomService.twoOfThree() ? Boolean.TRUE : Boolean.FALSE);
            log.info("Message " + message.getMessageId() + " is received");
            // обработка сообщения
            messageProcessingService.process(message);
            log.info("Message " + message.getMessageId() + " processed");
        } catch (Exception e) {
            // если получение или обработка сообщения упали с ошибкой,
            // следовательно сообщение обработается повторно
            log.error(e.getMessage());
            messageService.addMessage(message);
        }
    }

}
