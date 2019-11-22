package com.example.chat.dao.pojo;

import java.sql.Date;

/**
 * @author Piotr Heilman
 */
public class UserEntity {
    private final Date createdAt;
    private final String username;

    public UserEntity(final Date createdAt, final String username) {
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
