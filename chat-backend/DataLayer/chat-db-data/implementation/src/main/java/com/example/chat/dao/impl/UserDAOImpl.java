package com.example.chat.dao.impl;

import com.example.chat.dao.interfaces.UserDAO;
import com.example.chat.dao.pojo.UserEntity;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author Piotr Heilman
 */
@Repository(value = "userDAO")
public class UserDAOImpl implements UserDAO {
    private final ConcurrentMap<String, UserEntity> users;

    public UserDAOImpl() {
        this.users = new ConcurrentHashMap<>();
    }

    @Override
    public UserEntity createUser(final String username) {
        if (users.containsKey(username)) {
            return null;
        }

        final UserEntity userEntity = new UserEntity(
                new Date(System.currentTimeMillis()), username
        );

        users.put(username, userEntity);

        return userEntity;
    }

    @Override
    public UserEntity findByUsername(final String username) {
        if (!users.containsKey(username)) {
            return null;
        }

        return users.get(username);
    }
}