package com.test.joselazo.userservices.controller;

import com.test.joselazo.userservices.dto.RequestUserLists;
import com.test.joselazo.userservices.dto.User;
import com.test.joselazo.userservices.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/user")
public class UserController {

    @Autowired
    private final UserService userService;
    private static User lastRetrievedUser;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> save(@RequestBody User user) {
        if (userService.emailExists(user.getEmail())) {
            throw new IllegalArgumentException("Duplicate E-Mail is not allowed");
        }
        userService.save(user);
        lastRetrievedUser = user;
        return new ResponseEntity<>(lastRetrievedUser, HttpStatus.CREATED);
    }

    @PostMapping(value = "/list", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> saveUsers(@RequestBody RequestUserLists requestUserLists) {
        List<User> users = requestUserLists.getUsers();

        for (User user : users) {
            if (userService.emailExists(user.getEmail())) {
                throw new IllegalArgumentException("Duplicate E-Mail is not allowed");
            }
            userService.save(user);
        }

        return new ResponseEntity<>("Users successfully inserted", HttpStatus.CREATED);
    }

    @GetMapping(value = "/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> getUser(@PathVariable String userId) {
        if (lastRetrievedUser != null && lastRetrievedUser.getId().equals(userId)) {
            System.out.println("Last User Retrieved from cache = { " + lastRetrievedUser + " }");
            return ResponseEntity.ok(lastRetrievedUser);
        }
        User user = userService.getUser(userId);
        lastRetrievedUser = user;
        System.out.println("New User query = { " + user + " }");
        return ResponseEntity.ok(user);
    }

    @DeleteMapping(value = "/{userId}")
    public void deleteUser(@PathVariable String userId) {
        userService.deleteUser(userId);
    }

    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
