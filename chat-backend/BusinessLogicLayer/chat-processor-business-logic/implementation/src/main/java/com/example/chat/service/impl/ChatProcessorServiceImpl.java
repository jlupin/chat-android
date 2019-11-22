package com.example.chat.service.impl;

import com.example.chat.bean.interfaces.ChatChannelStoreBean;
import com.example.chat.common.pojo.Message;
import com.example.chat.common.pojo.User;
import com.example.chat.service.interfaces.ChatProcessorService;
import com.example.chat.service.pojo.ChatDetails;
import com.jlupin.impl.client.util.channel.JLupinClientChannelUtil;
import com.jlupin.impl.client.util.channel.exception.JLupinClientChannelUtilException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * @author Piotr Heilman
 */
@Service(value = "chatProcessorService")
public class ChatProcessorServiceImpl implements ChatProcessorService {
    private static final Logger logger = LoggerFactory.getLogger(ChatProcessorServiceImpl.class);

    @Autowired
    private ChatChannelStoreBean chatChannelStoreBean;
    @Autowired
    private JLupinClientChannelUtil jlupinClientChannelUtil;

    @Override
    public ChatDetails connectToChat(final String channelName, final User user) throws Exception {
        final String channelStreamId = chatChannelStoreBean.addUserToChannel(channelName, user);

        return new ChatDetails(channelStreamId);
    }

    @Override
    public void sendMessage(final String channelName, final Message message) {
        final List<String> allStreamIdsForChannel = chatChannelStoreBean.getAllStreamIdsForChannel(channelName);

        if (Objects.isNull(allStreamIdsForChannel)) {
            return;
        }

        for (final String channelStreamId : allStreamIdsForChannel) {
            try {
                jlupinClientChannelUtil.putNextElementToStreamChannel(
                        channelStreamId, message
                );
            } catch (JLupinClientChannelUtilException e) {
                logger.error("Error sending message to streamId: " + channelStreamId, e);
            }
        }
    }

    public void closeStream(final String channelStreamId) {
        chatChannelStoreBean.removeChannelStreamId(channelStreamId);
        try {
            jlupinClientChannelUtil.closeStreamChannel(channelStreamId);
        } catch (JLupinClientChannelUtilException e) {
            logger.error("Error while closing streamId: " + channelStreamId, e);
        }
    }
}