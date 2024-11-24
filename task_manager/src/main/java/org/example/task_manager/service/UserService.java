package org.example.task_manager.service;

import org.example.task_manager.dto.SingInRequest;
import org.example.task_manager.dto.SingUpRequest;
import org.example.task_manager.enums.RoleEnum;
import org.example.task_manager.models.User;
import org.example.task_manager.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void saveUser(SingUpRequest singUpRequest) {

        String hashPassword = passwordEncoder.encode(singUpRequest.getPassword());

        User user = new User();

        user.setEmail(singUpRequest.getEmail());
        user.setHashPassword(hashPassword);
        user.setRole(RoleEnum.valueOf(singUpRequest.getRole()));

        userRepository.save(user);
    }

    public boolean existsUsersByEmail(String email) {
        return userRepository.existsUsersByEmail(email);
    }

    public boolean checkUserPassword(SingInRequest singInRequest) {
        User user = userRepository.findUserByEmail(singInRequest.getEmail());

        return passwordEncoder.matches(singInRequest.getPassword(), user.getHashPassword());
    }


}
