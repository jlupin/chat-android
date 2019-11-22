package com.example.chat.service.impl;

import com.example.chat.common.pojo.User;
import com.example.chat.dao.interfaces.UserDAO;
import com.example.chat.dao.pojo.UserEntity;
import com.example.chat.service.interfaces.UserDBService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @author Piotr Heilman
 */
@Service(value = "userDBService")
public class UserDBServiceImpl implements UserDBService {
    @Autowired
    private UserDAO userDAO;

    @Override
    public User findByUsername(final String username) {
        final UserEntity userEntity = userDAO.findByUsername(username);
        return convertUserEntity(userEntity);
    }

    private User convertUserEntity(final UserEntity userEntity) {
        if (Objects.isNull(userEntity)) {
            return null;
        }

        return new User(
                userEntity.getCreatedAt(),
                userEntity.getUsername()
        );
    }
}