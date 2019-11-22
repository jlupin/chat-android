package com.example.chat.service.pojo;

import java.io.Serializable;

/**
 * @author Piotr Heilman
 */
public class ChatDetails implements Serializable {
    private final String channelStreamId;

    public ChatDetails(final String channelStreamId) {
        this.channelStreamId = channelStreamId;
    }

    public String getChannelStreamId() {
        return channelStreamId;
    }
}
