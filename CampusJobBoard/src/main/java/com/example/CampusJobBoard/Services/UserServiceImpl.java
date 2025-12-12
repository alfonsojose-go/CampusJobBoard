package com.example.CampusJobBoard.Services;

import com.example.CampusJobBoard.dto.UserRegistrationDto;
import com.example.CampusJobBoard.Models.User;
import com.example.CampusJobBoard.Repositories.UserRepo;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepo userRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User registerNewUser(UserRegistrationDto dto) {

        // 1. Check if email already exists
        if (userRepo.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("This email is already registered. Please log in.");
        }

        // 2. Validate & convert role string → enum
        User.Role roleEnum;
        try {
            roleEnum = User.Role.valueOf(dto.getRole().toUpperCase().trim());
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid role. Use STUDENT or EMPLOYER");
        }

        // 3. Create new User entity
        User user = new User();
        user.setFullName(dto.getFullName());
        user.setEmail(dto.getEmail());
        // encrypt password with BCrypt
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRole(roleEnum);
        user.setStatus(User.UserStatus.ACTIVE);

        // 4. Save and return
        return userRepo.save(user);
    }
}
