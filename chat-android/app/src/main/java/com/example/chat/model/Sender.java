package com.example.chat.model;

/**
 * @author Piotr Heilman
 */
public class Sender {
    private final String username;

    public Sender(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
