package org.example.task_manager_work.services;

import org.example.task_manager_work.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean existUserByUserId(int id) {
        return userRepository.existsUsersByUserId(id);
    }

    public boolean checkRoleAdmin(String role) {
        return Objects.equals(role, "ADMIN");
    }
}
