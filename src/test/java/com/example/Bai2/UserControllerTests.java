package com.example.Bai2;

import com.example.Bai2.model.User;
import com.example.Bai2.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserControllerTests {

    @Autowired
    private UserService userService;

    private User testUser;

    @BeforeEach
    public void setUp() {
        testUser = new User("John Doe", "john@example.com", "0123456789");
    }

    @Test
    public void testCreateUser() {
        User createdUser = userService.createUser(testUser);
        assertNotNull(createdUser);
        assertNotNull(createdUser.getId());
        assertEquals("John Doe", createdUser.getName());
        assertEquals("john@example.com", createdUser.getEmail());
    }

    @Test
    public void testGetAllUsers() {
        userService.createUser(testUser);
        assertNotNull(userService.getAllUsers());
        assertTrue(userService.getAllUsers().size() > 0);
    }

    @Test
    public void testGetUserById() {
        User savedUser = userService.createUser(testUser);
        User foundUser = userService.getUserById(savedUser.getId()).orElse(null);
        assertNotNull(foundUser);
        assertEquals("John Doe", foundUser.getName());
    }

    @Test
    public void testGetUserByEmail() {
        userService.createUser(testUser);
        User foundUser = userService.getUserByEmail("john@example.com").orElse(null);
        assertNotNull(foundUser);
        assertEquals("John Doe", foundUser.getName());
    }

    @Test
    public void testUpdateUser() {
        User savedUser = userService.createUser(testUser);
        User updatedUser = new User("Jane Doe", "jane@example.com", "0987654321");
        User result = userService.updateUser(savedUser.getId(), updatedUser);
        assertNotNull(result);
        assertEquals("Jane Doe", result.getName());
        assertEquals("jane@example.com", result.getEmail());
    }

    @Test
    public void testDeleteUser() {
        User savedUser = userService.createUser(testUser);
        boolean deleted = userService.deleteUser(savedUser.getId());
        assertTrue(deleted);
        assertFalse(userService.getUserById(savedUser.getId()).isPresent());
    }
}
