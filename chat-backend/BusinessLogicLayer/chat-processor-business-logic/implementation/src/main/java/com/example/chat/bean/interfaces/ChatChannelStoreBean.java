package com.example.chat.bean.interfaces;

import com.example.chat.common.pojo.User;

import java.util.List;

/**
 * @author Piotr Heilman
 */
public interface ChatChannelStoreBean {
    String addUserToChannel(final String channelName, final User user) throws Exception;

    List<String> getAllStreamIdsForChannel(final String channelName);
    void removeChannelStreamId(final String channelStreamId);
}