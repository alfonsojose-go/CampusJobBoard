package com.example.CampusJobBoard.Services;

import com.example.CampusJobBoard.Models.JobApplication;
import com.example.CampusJobBoard.Models.User;
import com.example.CampusJobBoard.Repositories.JobApplicationRepo;
import com.example.CampusJobBoard.Repositories.UserRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentServiceImpl implements StudentService {

    private final JobApplicationRepo applicationRepo;
    private final UserRepo userRepository;

    public StudentServiceImpl(JobApplicationRepo applicationRepo,
                              UserRepo userRepository) {
        this.applicationRepo = applicationRepo;
        this.userRepository = userRepository;
    }

    @Override
    public List<JobApplication> getMyApplications(String email) {

        // find the student by email
        User student = userRepository.findByEmail(email)
                .orElse(null);

        if (student == null) {
            return List.of(); // return empty list
        }

        return applicationRepo.findByStudent_UserId(student.getUserId());
    }
}
