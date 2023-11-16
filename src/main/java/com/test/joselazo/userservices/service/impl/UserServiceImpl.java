package com.test.joselazo.userservices.service.impl;

import com.test.joselazo.userservices.dto.User;
import com.test.joselazo.userservices.service.UserService;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class UserServiceImpl implements UserService {
    private final Map<String, User> users = new ConcurrentHashMap<>();

    @Override
    public User save(User user) {
        user.setId(generateUniqueId());
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User getUser(String userId) {
        return users.get(userId);
    }

    @Override
    public void deleteUser(String userId) {
        users.remove(userId);
    }

    @Override
    public boolean emailExists(String email) {
        return users.values().stream().anyMatch(user -> user.getEmail().equals(email));
    }

    private String generateUniqueId() {
        return String.valueOf(10000000L + ((long) (new Random().nextDouble() * (99999999L - 10000000L))));
    }

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }
}
