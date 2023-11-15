package com.test.joselazo.userservices.service;

import com.test.joselazo.userservices.dto.User;

import java.util.List;

public interface UserService {

    User save(User user);

    User getUser(String userId);

    void deleteUser(String userId);

    boolean emailExists(String email);

    List<User> getAllUsers();
}
