package com.example.chat.configuration;

import com.example.chat.service.interfaces.UserDBService;
import com.jlupin.impl.client.util.JLupinClientUtil;
import com.jlupin.impl.client.util.channel.JLupinClientChannelUtil;
import com.jlupin.interfaces.client.delegator.JLupinDelegator;
import com.jlupin.interfaces.common.enums.PortType;
import com.jlupin.interfaces.microservice.partofjlupin.asynchronous.service.channel.JLupinChannelManagerService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Piotr Heilman
 */
@Configuration
@ComponentScan("com.example.chat")
public class ChatProcessorSpringConfiguration {
    @Bean
    public JLupinDelegator getJLupinDelegator() {
        return JLupinClientUtil.generateInnerMicroserviceLoadBalancerDelegator(PortType.JLRMC);
    }

    @Bean
    public JLupinChannelManagerService getJLupinChannelManagerService() {
        return JLupinClientUtil.generateRemote(getJLupinDelegator(), "channelMicroservice", "jLupinChannelManagerService" , JLupinChannelManagerService.class);
    }

    @Bean
    public JLupinClientChannelUtil getJLupinClientChannelUtil() {
        return new JLupinClientChannelUtil("CHAT_CHANNEL", getJLupinChannelManagerService());
    }

    @Bean
    public UserDBService getUserDBService() {
        return JLupinClientUtil.generateRemote(getJLupinDelegator(), "chat-db", UserDBService.class);
    }

    @Bean(name = "jLupinRegularExpressionToRemotelyEnabled")
    public List getRemotelyBeanList() {
        List<String> list = new ArrayList<>();
        list.add("chatProcessorService");
        list.add("userService");
        return list;
    }
}

