package com.example.chat.websocket.handler;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author Piotr Heilman
 */
@Component
public class ChannelWebSocketStore {
    private ConcurrentMap<String, ConcurrentMap<String, Object>> sessionStore;

    public ChannelWebSocketStore() {
        sessionStore = new ConcurrentHashMap<>();
    }

    public void addToSession(final WebSocketSession session, final String key, final Object obj) {
        final ConcurrentMap<String, Object> currentSessionStore = sessionStore.get(session.getId());

        if (Objects.isNull(currentSessionStore)) {
            return;
        }

        currentSessionStore.put(key, obj);
    }

    public Object getFromSession(final WebSocketSession session, final String key) {
        final ConcurrentMap<String, Object> currentSessionStore = sessionStore.get(session.getId());

        if (Objects.isNull(currentSessionStore)) {
            return null;
        }

        return currentSessionStore.get(key);
    }

    public void newSession(final WebSocketSession session) {
        sessionStore.put(session.getId(), new ConcurrentHashMap<>());
    }

    public void removedSession(final WebSocketSession session) {
        sessionStore.remove(session.getId());
    }
}
