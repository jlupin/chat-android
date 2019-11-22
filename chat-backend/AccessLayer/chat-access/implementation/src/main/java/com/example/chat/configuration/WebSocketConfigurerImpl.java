package com.example.chat.configuration;

import com.example.chat.websocket.handler.ChannelWebSocketHandlerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

/**
 * @author Piotr Heilman
 */
@Configuration
@EnableWebSocket
public class WebSocketConfigurerImpl implements WebSocketConfigurer {
    @Autowired
    private ChannelWebSocketHandlerImpl channelWebSocketHandler;

    @Override
    public void registerWebSocketHandlers(final WebSocketHandlerRegistry webSocketHandlerRegistry) {
        webSocketHandlerRegistry.addHandler(
                channelWebSocketHandler, channelWebSocketHandler.getEndpoint()
        ).setAllowedOrigins("*");
    }
}
