package com.example.CampusJobBoard.Services;

import com.example.CampusJobBoard.dto.UserRegistrationDto;
import com.example.CampusJobBoard.Models.User;

public interface UserService {
    User registerNewUser(UserRegistrationDto dto);
}
