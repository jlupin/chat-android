package com.example.chat.service.impl;

import com.example.chat.service.interfaces.ChatService;
import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketFactory;

/**
 * @author Piotr Heilman
 */
public class ChatServiceImpl implements ChatService {
    private final String channelName;
    private final String username;

    private WebSocket ws;

    public ChatServiceImpl(
            final String channelName, final String username
    ) {
        this.channelName = channelName;
        this.username = username;
    }

    @Override
    public void connect(final NewMessageCallback onNewMessage) throws Exception {
        if (ws != null) {
            return;
        }

        ws = new WebSocketFactory().createSocket("ws://10.0.2.2:8000/chat/channel");
        ws.setPingInterval(1000);

        ws.addListener(new WebSocketAdapter() {
            @Override
            public void onTextMessage(final WebSocket websocket, final String text) throws Exception {
                onNewMessage.call(text);
            }
        });

        ws.connect();
        ws.sendText(username);
        ws.flush();
        ws.sendText(channelName);
        ws.flush();
    }

    public void sendMessage(final String message) throws Exception {
        ws.sendText(message);
        ws.flush();
    }
}
