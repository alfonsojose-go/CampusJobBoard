package com.example.CampusJobBoard.Services;

import com.example.CampusJobBoard.Models.Job;
import com.example.CampusJobBoard.Models.JobApplication;
import com.example.CampusJobBoard.Models.User;
import com.example.CampusJobBoard.Repositories.JobApplicationRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ApplicationService {

    private final JobApplicationRepo applicationRepo;
    private final JobRepo jobRepository;
    private final UserRepo userRepository;

    @Autowired
    public ApplicationService(JobApplicationRepo applicationRepo,
                              JobRepo jobRepository,
                              UserRepo userRepository) {
        this.applicationRepo = applicationRepo;
        this.jobRepository = jobRepository;
        this.userRepository = userRepository;
    }

    // =======================================
    // APPLY TO JOB (Student functionality)
    // =======================================
    public void applyToJob(Long jobId, String studentEmail) {

        // 1. Find student from email (Principal)
        User student = userRepository.findByEmail(studentEmail)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        // 2. Validate job exists
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        // 3. Check if student already applied (duplicate prevention)
        boolean alreadyApplied =
                applicationRepo.existsByJobJobIdAndStudentUserId(jobId, student.getUserId());

        if (alreadyApplied) {
            throw new RuntimeException("You have already applied to this job.");
        }

        // 4. Create a new JobApplication entity
        JobApplication application = new JobApplication();
        application.setJob(job);
        application.setStudent(student);

        // IMPORTANT: Use the ENUM, not a String
        application.setStatus(JobApplication.ApplicationStatus.SUBMITTED);

        // 5. Save new application
        applicationRepo.save(application);
    }
}
