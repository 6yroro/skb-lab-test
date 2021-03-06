package com.test.skblab.services;

import com.test.skblab.messaging.Message;
import com.test.skblab.messaging.MessageId;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Alexander Zubkov
 * Эмуляция очереди сообщений
 */
@Service
public class MessageService<T> {

    private ConcurrentHashMap<MessageId, Message<T>> messages = new ConcurrentHashMap<>();

    void addMessage(Message<T> message) {
        messages.put(message.getMessageId(), message);
    }

    void removeMessage(Message<T> message) {
        messages.remove(message.getMessageId());
    }

    public ConcurrentHashMap<MessageId, Message<T>> getMessages() {
        return messages;
    }

}
