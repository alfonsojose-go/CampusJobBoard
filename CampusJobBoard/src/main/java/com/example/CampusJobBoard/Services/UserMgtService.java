package com.example.CampusJobBoard.Services;

import com.example.CampusJobBoard.Models.User;

import java.util.List;
import java.util.Optional;

public interface UserMgtService {
    List<User> findAll();

    Optional<User> findById(Long id);

    User save(User user);

}
