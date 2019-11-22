package com.example.chat.bean.impl;

import com.example.chat.bean.interfaces.ChatChannelStoreBean;
import com.example.chat.common.pojo.User;
import com.jlupin.impl.client.util.channel.JLupinClientChannelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author Piotr Heilman
 */
@Service(value = "chatChannelStoreBean")
public class ChatChannelStoreBeanImpl implements ChatChannelStoreBean {
    @Autowired
    private JLupinClientChannelUtil jlupinClientChannelUtil;

    private ConcurrentMap<String, List<String>> channelNameToStreamIds;

    public ChatChannelStoreBeanImpl() {
        this.channelNameToStreamIds = new ConcurrentHashMap<>();
    }

    @Override
    public String addUserToChannel(final String channelName, final User user) throws Exception {
        channelNameToStreamIds.putIfAbsent(channelName, new ArrayList<>());
        final List<String> streamIds = channelNameToStreamIds.get(channelName);

        final String streamId = jlupinClientChannelUtil.openStreamChannel();

        streamIds.add(streamId);

        return streamId;
    }

    @Override
    public List<String> getAllStreamIdsForChannel(final String channelName) {
        return new ArrayList<>(channelNameToStreamIds.getOrDefault(channelName, Collections.emptyList()));
    }

    @Override
    public void removeChannelStreamId(final String channelStreamId) {
        for (final List<String> streamIds : channelNameToStreamIds.values()) {
            streamIds.remove(channelStreamId);
        }
    }
}