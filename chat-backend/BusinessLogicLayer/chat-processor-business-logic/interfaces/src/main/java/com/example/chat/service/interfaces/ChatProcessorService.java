package com.example.chat.service.interfaces;

import com.example.chat.common.pojo.Message;
import com.example.chat.common.pojo.User;
import com.example.chat.service.pojo.ChatDetails;

/**
 * @author Piotr Heilman
 */
public interface ChatProcessorService {
    ChatDetails connectToChat(final String channelName, final User user) throws Exception;

    void sendMessage(final String channelName, final Message message);
    void closeStream(final String channelStreamId);
}