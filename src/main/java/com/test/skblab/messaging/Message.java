package com.test.skblab.messaging;

/**
 * @author Alexander Zubkov
 */
public class Message<T> {

    private MessageId messageId;

    private T messageData;

    public Message(T t) {
        messageData = t;
    }

    public MessageId getMessageId() {
        return messageId;
    }

    public void setMessageId(MessageId messageId) {
        this.messageId = messageId;
    }

    public T getMessageData() {
        return messageData;
    }
}
