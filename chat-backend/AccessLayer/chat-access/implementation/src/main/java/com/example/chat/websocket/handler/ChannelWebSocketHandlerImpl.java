package com.example.chat.websocket.handler;

import com.example.chat.common.pojo.Message;
import com.example.chat.common.pojo.User;
import com.example.chat.service.interfaces.ChatProcessorService;
import com.example.chat.service.interfaces.UserService;
import com.example.chat.service.pojo.ChatDetails;
import com.jlupin.impl.client.util.channel.JLupinClientChannelIterableProducer;
import com.jlupin.impl.client.util.publisher.JLupinClientPublisherUtil;
import com.jlupin.impl.client.util.publisher.exception.JLupinClientPublisherUtilException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.sql.Date;
import java.util.Iterator;
import java.util.Objects;

/**
 * @author Piotr Heilman
 */
@Component
public class ChannelWebSocketHandlerImpl extends TextWebSocketHandler {
    private static final Logger logger = LoggerFactory.getLogger(ChannelWebSocketHandlerImpl.class);

    private static final String ENDPOINT = "/channel";

    @Autowired
    private JLupinClientPublisherUtil jlupinClientPublisherUtil;
    @Autowired
    private JLupinClientChannelIterableProducer jlupinClientChannelIterableProducer;
    @Autowired
    private ChatProcessorService chatProcessorService;
    @Autowired
    private UserService userService;
    @Autowired
    private ChannelWebSocketStore webSocketStore;

    public String getEndpoint() {
        return ENDPOINT;
    }

    @Override
    public void afterConnectionEstablished(final WebSocketSession session) throws Exception {
        logger.debug("\n[{}] New websocket session",  session.getId());

        final ChannelWebSocketContext webSocketContext = new ChannelWebSocketContext(
                webSocketStore, session
        );
        webSocketContext.create();

        webSocketContext.setCurrentState(ChannelWebSocketState.SELECT_USER);
    }

    @Override
    public void afterConnectionClosed(final WebSocketSession session, final CloseStatus status) throws Exception {
        logger.debug("\n[{}] Closed websocket session", session.getId());

        final ChannelWebSocketContext webSocketContext = new ChannelWebSocketContext(
                webSocketStore, session
        );

        try {
            closeChanel(webSocketContext);
        } finally {
            webSocketContext.remove();
        }
    }

    @Override
    protected void handleTextMessage(final WebSocketSession session, final TextMessage message) throws Exception {
        final String messageText = message.getPayload();

        logger.debug("\n[{}] New message received: {}", session.getId(), messageText);

        final ChannelWebSocketContext webSocketContext = new ChannelWebSocketContext(
                webSocketStore, session
        );

        final ChannelWebSocketState state = webSocketContext.getCurrentState();

        if (Objects.isNull(state)) {
            throw new Exception("State should be set");
        }

        switch (state) {
            case SELECT_USER:
                loginUser(webSocketContext, messageText);
                break;
            case SELECT_CHANNEL:
                joinChannel(webSocketContext, messageText);
                break;
            case RECEIVE_MESSAGE:
                sendMessage(webSocketContext, messageText);
                break;
            default:
                throw new Exception("Unsupported state");
        }
    }

    private void loginUser(final ChannelWebSocketContext webSocketContext, final String username) throws Exception {
        final User user = userService.getUserByUsername(username);

        if (Objects.isNull(user)) {
            throw new Exception("User not found");
        }

        webSocketContext.setCurrentUser(user);
        webSocketContext.setCurrentState(ChannelWebSocketState.SELECT_CHANNEL);
    }

    private void joinChannel(
            final ChannelWebSocketContext webSocketContext, final String channelName
    ) throws Exception {
        final User user = webSocketContext.getCurrentUser();
        final ChatDetails chatDetails = chatProcessorService.connectToChat(channelName, user);

        webSocketContext.setCurrentChatDetails(chatDetails);

        final WebSocketSession session = webSocketContext.getWebSocketSession();

        final Thread t = new Thread(() -> {
            final Iterable iterable = jlupinClientChannelIterableProducer.produceChannelIterable(
                    3600000L, 1000L, "CHAT_CHANNEL", chatDetails.getChannelStreamId()
            );
            final Iterator iterator = iterable.iterator();

            logger.debug("\n[{}] Start listening for messages", session.getId());

            while (iterator.hasNext()) {
                Message message = (Message) iterator.next();
                try {
                    logger.debug("\n[{}] Sending message: {}",  session.getId(), message.getMessage());

                    session.sendMessage(new TextMessage(
                            "{" +
                            "\"created_at\":" + message.getCreatedAt().getTime() + "," +
                            "\"message\":\"" + message.getMessage() + "\"," +
                            "\"user\":\"" + message.getUser().getUsername() + "\"" +
                            "}"
                    ));
                } catch (IOException e) {
                    logger.error("Error sending message, closing session", e);
                    try {
                        session.close();
                    } catch (IOException ex) {
                        logger.error("Error closing session", e);
                    }
                }
            }

            logger.debug("\n[{}] Stop listening for messages", session.getId());
        });

        webSocketContext.setCurrentListeningThread(t);
        webSocketContext.setCurrentChannelName(channelName);

        t.start();

        webSocketContext.setCurrentState(ChannelWebSocketState.RECEIVE_MESSAGE);
    }

    private void sendMessage(
            final ChannelWebSocketContext webSocketContext, final String message
    ) throws Exception {
        final User user = webSocketContext.getCurrentUser();
        final String channelName = webSocketContext.getCurrentChannelName();

        try {
            jlupinClientPublisherUtil.publishTaskToAllSubscribers(
                    "chat-processor",
                    "chatProcessorService",
                    "sendMessage",
                    new Object[]{
                            channelName,
                            new Message(
                                    new Date(System.currentTimeMillis()),
                                    message,
                                    user
                            )
                    }
            );
        } catch (JLupinClientPublisherUtilException e) {
            throw new Exception("Error sending message to all subscribers", e);
        }
    }

    private void closeChanel(
            final ChannelWebSocketContext webSocketContext
    ) throws Exception {
        final ChatDetails chatDetails = webSocketContext.getCurrentChatDetails();
        chatProcessorService.closeStream(chatDetails.getChannelStreamId());

        final Thread currentListeningThread = webSocketContext.getCurrentListeningThread();
        currentListeningThread.join(1000);
        currentListeningThread.interrupt();
    }
}
