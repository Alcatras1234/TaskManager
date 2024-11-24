package org.example.task_manager.test;

import org.example.task_manager.dto.SingInRequest;
import org.example.task_manager.dto.SingUpRequest;
import org.example.task_manager.models.User;
import org.example.task_manager.repository.UserRepository;
import org.example.task_manager.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest
public class UserServiceTests {
    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        when(passwordEncoder.encode(anyString())).thenReturn("hashedPassword");
        when(passwordEncoder.matches(anyString(), eq("hashedPassword"))).thenReturn(true);
    }

    @Test
    void testSaveUser() {
        SingUpRequest signUpRequest = new SingUpRequest();
        signUpRequest.setEmail("test@example.com");
        signUpRequest.setPassword("password");
        signUpRequest.setRole("USER");

        userService.saveUser(signUpRequest);

        verify(userRepository, times(1)).save(any(User.class));
        verify(passwordEncoder, times(1)).encode("password");
    }

    @Test
    void testExistsUsersByEmail() {
        String email = "test@example.com";
        when(userRepository.existsUsersByEmail(email)).thenReturn(true);

        boolean exists = userService.existsUsersByEmail(email);

        assertTrue(exists);
        verify(userRepository, times(1)).existsUsersByEmail(email);
    }

    @Test
    void testCheckUserPassword() {
        String email = "test@example.com";
        String password = "password";

        User user = new User();
        user.setEmail(email);
        user.setHashPassword("hashedPassword");

        when(userRepository.findUserByEmail(email)).thenReturn(user);

        SingInRequest signInRequest = new SingInRequest();
        signInRequest.setEmail(email);
        signInRequest.setPassword(password);

        boolean matches = userService.checkUserPassword(signInRequest);

        assertTrue(matches);
        verify(userRepository, times(1)).findUserByEmail(email);
        verify(passwordEncoder, times(1)).matches(password, "hashedPassword");
    }
}

