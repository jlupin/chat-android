package com.example.chat.common.pojo;

import java.io.Serializable;
import java.sql.Date;

/**
 * @author Piotr Heilman
 */
public class Message implements Serializable {
    private final Date createdAt;
    private final String message;
    private final User user;

    public Message(final Date createdAt, final String message, final User user) {
        this.createdAt = createdAt;
        this.message = message;
        this.user = user;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public String getMessage() {
        return message;
    }

    public User getUser() {
        return user;
    }
}
