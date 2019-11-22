package com.example.chat.model;

import java.util.Date;

/**
 * @author Piotr Heilman
 */
public class Message {
    private final Date createdAt;
    private final String message;
    private final Sender sender;

    public Message(final Date createdAt, final String message, final Sender sender) {
        this.createdAt = createdAt;
        this.message = message;
        this.sender = sender;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public String getMessage() {
        return message;
    }

    public Sender getSender() {
        return sender;
    }
}
