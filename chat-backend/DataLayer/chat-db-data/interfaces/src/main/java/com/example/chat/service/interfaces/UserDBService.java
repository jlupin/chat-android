package com.example.chat.service.interfaces;

import com.example.chat.common.pojo.User;

/**
 * @author Piotr Heilman
 */
public interface UserDBService {
    User findByUsername(final String username);
}