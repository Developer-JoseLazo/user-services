package com.test.joselazo.userservices.controller;

import com.test.joselazo.userservices.dto.RequestUserLists;
import com.test.joselazo.userservices.dto.User;
import com.test.joselazo.userservices.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    private final User user = new User("56+56+", "test@example.com", "Test User");

    @Test
    void save_ValidUser_ReturnsCreatedResponse() {
        when(userService.emailExists(user.getEmail())).thenReturn(false);
        when(userService.save(any(User.class))).thenReturn(user);

        ResponseEntity<User> responseEntity = userController.save(user);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(user, responseEntity.getBody());
        verify(userService, times(1)).save(user);
    }

    @Test
    void save_DuplicateEmail_ThrowsException() {
        when(userService.emailExists(user.getEmail())).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> userController.save(user));

        verify(userService, never()).save(user);
    }

    @Test
    void saveUsers_ValidUsers_ReturnsCreatedResponse() {
        RequestUserLists requestUserLists = new RequestUserLists(Collections.singletonList(new User("", "test@example.com", "Test User")));

        when(userService.emailExists(anyString())).thenReturn(false);
        when(userService.save(any(User.class))).thenReturn(new User());

        ResponseEntity<String> responseEntity = userController.saveUsers(requestUserLists);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals("Users successfully inserted", responseEntity.getBody());
        verify(userService, times(1)).save(any(User.class));
    }


    @Test
    void getUser_LastRetrievedUserExists_ReturnsCachedUser() {
        User cachedUser = new User("", "cached@example.com", "Cached User");
        userController.save(cachedUser);

        ResponseEntity<User> responseEntity = userController.getUser(cachedUser.getId());

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(cachedUser, responseEntity.getBody());
    }

    @Test
    void getUser_LastRetrievedUserDoesNotExist_ReturnsUserFromService() {
        when(userService.getUser(anyString())).thenReturn(user);

        ResponseEntity<User> responseEntity = userController.getUser(user.getId());

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(user, responseEntity.getBody());
        verify(userService, times(1)).getUser(user.getId());
    }

    @Test
    void deleteUser_ValidUserId_DeletesUser() {
        String userId = "testUserId";

        userController.deleteUser(userId);

        verify(userService, times(1)).deleteUser(userId);
    }

    @Test
    void getAllUsers_ReturnsListOfUsers() {
        List<User> users = Arrays.asList(
                new User("123", "user1@example.com", "User 1"),
                new User("321", "user2@example.com", "User 2")
        );

        when(userService.getAllUsers()).thenReturn(users);

        ResponseEntity<List<User>> responseEntity = userController.getAllUsers();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(users, responseEntity.getBody());
        verify(userService, times(1)).getAllUsers();
    }

}
