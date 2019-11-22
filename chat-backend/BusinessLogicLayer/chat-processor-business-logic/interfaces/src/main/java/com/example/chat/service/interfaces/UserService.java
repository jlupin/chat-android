package com.example.chat.service.interfaces;

import com.example.chat.common.pojo.User;

/**
 * @author Piotr Heilman
 */
public interface UserService {
    User getUserByUsername(final String username);
}