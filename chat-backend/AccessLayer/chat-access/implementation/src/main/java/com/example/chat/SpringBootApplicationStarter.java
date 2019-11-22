package com.example.chat;

import com.example.chat.configuration.ChatSpringConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Piotr Heilman
 */
@SpringBootApplication
public class SpringBootApplicationStarter {
    public static void main(String[] args) throws Exception {
        SpringApplication.run(ChatSpringConfiguration.class, args);
    }
}

