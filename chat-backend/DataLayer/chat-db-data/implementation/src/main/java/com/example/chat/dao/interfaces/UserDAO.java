package com.example.chat.dao.interfaces;

import com.example.chat.dao.pojo.UserEntity;

/**
 * @author Piotr Heilman
 */
public interface UserDAO {
    UserEntity createUser(final String username);

    UserEntity findByUsername(final String username);
}