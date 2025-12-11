package com.example.CampusJobBoard.Services;

import com.example.CampusJobBoard.Models.JobApplication;
import com.example.CampusJobBoard.Models.User;
import com.example.CampusJobBoard.Repositories.JobApplicationRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService {

    private final JobApplicationRepo applicationRepo;
    private final UserRepo userRepository;

    @Autowired
    public StudentService(JobApplicationRepo applicationRepo,
                          UserRepo userRepository) {
        this.applicationRepo = applicationRepo;
        this.userRepository = userRepository;
    }

    public List<JobApplication> getMyApplications(String email) {

        User student = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        return applicationRepo.findByStudentUserId(student.getUserId());
    }
}
