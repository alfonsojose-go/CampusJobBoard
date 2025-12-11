package com.example.jobboard.service;

import com.example.jobboard.dto.UserRegistrationDto;
import com.example.jobboard.entity.User;

public interface UserService {
    User registerNewUser(UserRegistrationDto dto);
}
