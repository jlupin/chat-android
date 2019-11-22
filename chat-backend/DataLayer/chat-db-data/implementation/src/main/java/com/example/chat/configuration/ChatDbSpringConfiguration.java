package com.example.chat.configuration;

import com.example.chat.dao.interfaces.UserDAO;
import com.jlupin.impl.client.util.JLupinClientUtil;
import com.jlupin.interfaces.client.delegator.JLupinDelegator;
import com.jlupin.interfaces.common.enums.PortType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Piotr Heilman
 */
@Configuration
@ComponentScan("com.example.chat")
public class ChatDbSpringConfiguration {
    @Autowired
    private ApplicationContext applicationContext;

    @Bean
    public JLupinDelegator getJLupinDelegator() {
        return JLupinClientUtil.generateInnerMicroserviceLoadBalancerDelegator(PortType.JLRMC);
    }

    @Bean(name = "jLupinRegularExpressionToRemotelyEnabled")
    public List getRemotelyBeanList() {
        List<String> list = new ArrayList<>();
        list.add("userDBService");
        return list;
    }

    @PostConstruct
    public void afterStart() {
        final UserDAO userDAO = applicationContext.getBean(UserDAO.class);

        userDAO.createUser("peter");
        userDAO.createUser("thomas");
        userDAO.createUser("mark");
    }
}

