package com.example.jobboard.service.impl;

import com.example.jobboard.dto.UserRegistrationDto;
import com.example.jobboard.entity.User;
import com.example.jobboard.repository.UserRepository;
import com.example.jobboard.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User registerNewUser(UserRegistrationDto dto) throws IllegalArgumentException {

        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("Email already registered");
        }

        User.Role roleEnum;

        String role = dto.getRole();
        if (role == null || role.isBlank()) {
            roleEnum = User.Role.STUDENT;
        } else {
            roleEnum = User.Role.valueOf(role.toUpperCase().trim());
        }


        User user = new User();
        user.setFullName(dto.getFullName());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword())); // BCrypt
        user.setRole(roleEnum);
        user.setStatus(User.Status.ACTIVE);

        return userRepository.save(user);
    }
}
