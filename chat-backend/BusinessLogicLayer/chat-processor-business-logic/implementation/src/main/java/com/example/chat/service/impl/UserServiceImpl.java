package com.example.chat.service.impl;

import com.example.chat.common.pojo.User;
import com.example.chat.service.interfaces.UserDBService;
import com.example.chat.service.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Piotr Heilman
 */
@Service(value = "userService")
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDBService userDBService;

    @Override
    public User getUserByUsername(final String username) {
        return userDBService.findByUsername(username);
    }
}