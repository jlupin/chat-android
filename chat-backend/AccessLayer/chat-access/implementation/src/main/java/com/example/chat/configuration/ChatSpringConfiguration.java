package com.example.chat.configuration;

import com.example.chat.service.interfaces.ChatProcessorService;
import com.example.chat.service.interfaces.UserService;
import com.jlupin.impl.client.util.JLupinClientUtil;
import com.jlupin.impl.client.util.channel.JLupinClientChannelIterableProducer;
import com.jlupin.impl.client.util.publisher.JLupinClientPublisherUtil;
import com.jlupin.interfaces.client.delegator.JLupinDelegator;
import com.jlupin.interfaces.common.enums.PortType;
import com.jlupin.interfaces.microservice.partofjlupin.asynchronous.service.channel.JLupinChannelManagerService;
import com.jlupin.interfaces.microservice.partofjlupin.asynchronous.service.queue.JLupinQueueManagerService;
import com.jlupin.servlet.monitor.annotation.EnableJLupinSpringBootServletMonitor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author Piotr Heilman
 */
@Configuration
@ComponentScan("com.example.chat")
@EnableJLupinSpringBootServletMonitor
public class ChatSpringConfiguration {
    @Bean
    public JLupinDelegator getJLupinDelegator() {
        return JLupinClientUtil.generateInnerMicroserviceLoadBalancerDelegator(PortType.JLRMC);
    }

    @Bean
    public JLupinDelegator getQueueJLupinDelegator() {
        return JLupinClientUtil.generateInnerMicroserviceLoadBalancerDelegator(PortType.QUEUE);
    }

    @Bean
    public JLupinQueueManagerService getJLupinQueueManagerService() {
        return JLupinClientUtil.generateRemote(getQueueJLupinDelegator(), "queueMicroservice", "jLupinQueueManagerService", JLupinQueueManagerService.class);
    }

    @Bean
    public JLupinChannelManagerService getJLupinChannelManagerService() {
        return JLupinClientUtil.generateRemote(getJLupinDelegator(), "channelMicroservice", "jLupinChannelManagerService" , JLupinChannelManagerService.class);
    }

    @Bean
    public JLupinClientPublisherUtil getJLupinClientPublisherUtil() {
        return new JLupinClientPublisherUtil("CHAT_QUEUE", getJLupinQueueManagerService());
    }

    @Bean
    public JLupinClientChannelIterableProducer getJLupinClientChannelIterableProducer() {
        return new JLupinClientChannelIterableProducer(getJLupinChannelManagerService());
    }

    @Bean
    public ChatProcessorService getChatProcessorService() {
        return JLupinClientUtil.generateRemote(getJLupinDelegator(), "chat-processor", ChatProcessorService.class);
    }

    @Bean
    public UserService getUserService() {
        return JLupinClientUtil.generateRemote(getJLupinDelegator(), "chat-processor", UserService.class);
    }
}

