package com.example.chat.service.interfaces;

/**
 * @author Piotr Heilman
 */
public interface ChatService {
    void connect(final NewMessageCallback onNewMessage) throws Exception;
    void sendMessage(final String message) throws Exception;

    interface NewMessageCallback {
        void call(final String message) throws Exception;
    }
}
