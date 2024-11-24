package org.example.task_manager_work.tests;

import org.example.task_manager_work.repository.UserRepository;
import org.example.task_manager_work.services.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class UserServiceTest {

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Test
    void existUserByUserId_UserExists_ReturnsTrue() {
        int userId = 1;
        Mockito.when(userRepository.existsUsersByUserId(userId)).thenReturn(true);

        boolean result = userService.existUserByUserId(userId);

        assertTrue(result, "User should exist");
    }

    @Test
    void existUserByUserId_UserDoesNotExist_ReturnsFalse() {
        int userId = 99;
        Mockito.when(userRepository.existsUsersByUserId(userId)).thenReturn(false);

        boolean result = userService.existUserByUserId(userId);

        assertFalse(result, "User should not exist");
    }

    @Test
    void checkRoleAdmin_RoleIsAdmin_ReturnsTrue() {
        boolean result = userService.checkRoleAdmin("ADMIN");

        assertTrue(result, "Role should be ADMIN");
    }

    @Test
    void checkRoleAdmin_RoleIsNotAdmin_ReturnsFalse() {
        boolean result = userService.checkRoleAdmin("USER");

        assertFalse(result, "Role should not be ADMIN");
    }
}
