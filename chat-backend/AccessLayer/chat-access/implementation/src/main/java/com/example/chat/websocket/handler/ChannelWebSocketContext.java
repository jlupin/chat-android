package com.example.chat.websocket.handler;

import com.example.chat.common.pojo.User;
import com.example.chat.service.pojo.ChatDetails;
import org.springframework.web.socket.WebSocketSession;

import java.util.Objects;

/**
 * @author Piotr Heilman
 */
public class ChannelWebSocketContext {
    private ChannelWebSocketStore channelWebSocketStore;
    private WebSocketSession webSocketSession;

    public ChannelWebSocketContext(
            final ChannelWebSocketStore channelWebSocketStore, final WebSocketSession webSocketSession
    ) {
        this.channelWebSocketStore = channelWebSocketStore;
        this.webSocketSession = webSocketSession;
    }

    public WebSocketSession getWebSocketSession() {
        return webSocketSession;
    }

    public User getCurrentUser() throws Exception {
        final User user = (User) channelWebSocketStore.getFromSession(webSocketSession, "user");

        if (Objects.isNull(user)) {
            throw new Exception("User not selected");
        }

        return user;
    }

    public void setCurrentUser(final User user) throws Exception {
        final Object alreadySetUser = channelWebSocketStore.getFromSession(webSocketSession, "user");
        if (Objects.nonNull(alreadySetUser)) {
            throw new Exception("User already selected");
        }

        channelWebSocketStore.addToSession(webSocketSession, "user", user);
    }

    public String getCurrentChannelName() throws Exception {
        final String channelName = (String) channelWebSocketStore.getFromSession(webSocketSession, "channelName");

        if (Objects.isNull(channelName)) {
            throw new Exception("Channel name not selected");
        }

        return channelName;
    }

    public void setCurrentChannelName(final String channelName) throws Exception {
        final String alreadySetChannelName = (String) channelWebSocketStore.getFromSession(webSocketSession, "channelName");
        if (Objects.nonNull(alreadySetChannelName)) {
            throw new Exception("Channel name already selected");
        }

        channelWebSocketStore.addToSession(webSocketSession, "channelName", channelName);
    }

    public ChatDetails getCurrentChatDetails() throws Exception {
        final ChatDetails chatDetails = (ChatDetails) channelWebSocketStore.getFromSession(webSocketSession, "chatDetails");

        if (Objects.isNull(chatDetails)) {
            throw new Exception("Chat details not selected");
        }

        return chatDetails;
    }

    public void setCurrentChatDetails(final ChatDetails chatDetails) throws Exception {
        final ChatDetails alreadySetChatDetails = (ChatDetails) channelWebSocketStore.getFromSession(webSocketSession, "chatDetails");
        if (Objects.nonNull(alreadySetChatDetails)) {
            throw new Exception("Chat details already selected");
        }

        channelWebSocketStore.addToSession(webSocketSession, "chatDetails", chatDetails);
    }

    public Thread getCurrentListeningThread() throws Exception {
        final Thread listeningThread = (Thread) channelWebSocketStore.getFromSession(webSocketSession, "listeningThread");

        if (Objects.isNull(listeningThread)) {
            throw new Exception("Listening thread not set");
        }

        return listeningThread;
    }

    public void setCurrentListeningThread(final Thread listeningThread) throws Exception {
        final Thread alreadySetListeningThread = (Thread) channelWebSocketStore.getFromSession(webSocketSession, "listeningThread");
        if (Objects.nonNull(alreadySetListeningThread)) {
            throw new Exception("Listening thread already set");
        }

        channelWebSocketStore.addToSession(webSocketSession, "listeningThread", listeningThread);
    }

    public ChannelWebSocketState getCurrentState() throws Exception {
        final ChannelWebSocketState state = (ChannelWebSocketState) channelWebSocketStore.getFromSession(webSocketSession, "state");

        if (Objects.isNull(state)) {

            throw new Exception("State is not set");
        }

        return state;
    }

    public void setCurrentState(final ChannelWebSocketState state) throws Exception {
        final Object alreadySetState = channelWebSocketStore.getFromSession(webSocketSession, "state");

        if (Objects.isNull(alreadySetState)) {
            if (state.equals(ChannelWebSocketState.SELECT_USER)) {
                channelWebSocketStore.addToSession(webSocketSession, "state", state);
                return;
            }
        } else {
            if (
                    (
                            alreadySetState.equals(ChannelWebSocketState.SELECT_USER)
                                    &&
                                    state.equals(ChannelWebSocketState.SELECT_CHANNEL)
                    ) || (
                            alreadySetState.equals(ChannelWebSocketState.SELECT_CHANNEL)
                                    &&
                                    state.equals(ChannelWebSocketState.RECEIVE_MESSAGE)
                    )
            ) {
                channelWebSocketStore.addToSession(webSocketSession, "state", state);
                return;
            }
        }

        throw new Exception("State cannot transition from " + alreadySetState + " to " + state);
    }

    public void create() {
        channelWebSocketStore.newSession(webSocketSession);
    }

    public void remove() {
        channelWebSocketStore.removedSession(webSocketSession);
    }
}
