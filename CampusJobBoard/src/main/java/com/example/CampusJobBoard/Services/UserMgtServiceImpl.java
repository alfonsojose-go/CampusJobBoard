package com.example.CampusJobBoard.Services;

import com.example.CampusJobBoard.Models.User;
import com.example.CampusJobBoard.Repositories.UserRepo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserMgtServiceImpl implements UserMgtService{

    private final UserRepo userRepo;

    public UserMgtServiceImpl(UserRepo userRepo) {
        this.userRepo = userRepo;
    }


    @Override
    public List<User> findAll() {
        return userRepo.findAll();
    }

    @Override
    public Optional<User> findById(Long id) {
        return userRepo.findById(id);
    }

    @Override
    public User save(User user) {
        return userRepo.save(user);
    }
}
