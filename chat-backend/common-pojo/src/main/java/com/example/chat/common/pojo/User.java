package com.example.chat.common.pojo;

import java.io.Serializable;
import java.sql.Date;

/**
 * @author Piotr Heilman
 */
public class User implements Serializable {
    private final Date createdAt;
    private final String username;

    public User(final Date createdAt, final String username) {
        this.createdAt = createdAt;
        this.username = username;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public String getUsername() {
        return username;
    }
}
