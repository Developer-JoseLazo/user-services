package com.test.joselazo.userservices.service;

import com.test.joselazo.userservices.dto.User;
import com.test.joselazo.userservices.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTest {

    private UserServiceImpl userServiceImpl;

    private final User user = new User("56+56+", "test@example.com", "Test User");

    @BeforeEach
    void setUp() {
        userServiceImpl = new UserServiceImpl();
    }

    @Test
    void saveUser() {
        User savedUser = userServiceImpl.save(user);

        assertNotNull(savedUser.getId());
        assertEquals(user.getEmail(), savedUser.getEmail());
        assertEquals(user.getName(), savedUser.getName());
    }

    @Test
    void getUser() {
        User savedUser = userServiceImpl.save(user);

        User retrievedUser = userServiceImpl.getUser(savedUser.getId());

        assertNotNull(retrievedUser);
        assertEquals(savedUser, retrievedUser);
    }

    @Test
    void deleteUser() {
        User savedUser = userServiceImpl.save(user);

        userServiceImpl.deleteUser(savedUser.getId());

        assertNull(userServiceImpl.getUser(savedUser.getId()));
    }

    @Test
    void emailExists() {
        userServiceImpl.save(user);

        assertTrue(userServiceImpl.emailExists("test@example.com"));
        assertFalse(userServiceImpl.emailExists("nonexistent@example.com"));
    }

    @Test
    void getAllUsers() {
        User user1 = new User("4567", "test@example.com", "User Name 1");

        User user2 = new User("5656", "test@example.com", "User Name 2");

        userServiceImpl.save(user1);
        userServiceImpl.save(user2);

        List<User> allUsers = userServiceImpl.getAllUsers();

        assertEquals(2, allUsers.size());
        assertTrue(allUsers.contains(user1));
        assertTrue(allUsers.contains(user2));
    }
}
