package com.test.skblab.messaging;

import java.util.UUID;

/**
 * @author Alexander Zubkov
 */
public class MessageId {

    private UUID id;

    public MessageId(UUID uuid) {
        id = uuid;
    }

    public UUID getId() {
        return id;
    }

    @Override
    public String toString() {
        return id.toString();
    }
}
