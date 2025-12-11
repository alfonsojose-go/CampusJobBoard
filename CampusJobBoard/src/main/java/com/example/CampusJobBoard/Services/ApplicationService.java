package com.example.CampusJobBoard.Services;

import com.example.CampusJobBoard.Models.Job;
import com.example.CampusJobBoard.Models.JobApplication;
import com.example.CampusJobBoard.Models.User;
import com.example.CampusJobBoard.Repositories.JobApplicationRepo;
import com.example.CampusJobBoard.Repositories.JobRepo;
import com.example.CampusJobBoard.Repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ApplicationService {

    private final JobApplicationRepo applicationRepo;
    private final JobRepo jobRepo;
    private final UserRepo userRepo;

    @Autowired
    public ApplicationService(JobApplicationRepo applicationRepo,
                              JobRepo jobRepo,
                              UserRepo userRepo) {
        this.applicationRepo = applicationRepo;
        this.jobRepo = jobRepo;
        this.userRepo = userRepo;
    }

    // -------------------------------------------------
    // APPLY TO A JOB  (this is where your method goes)
    // -------------------------------------------------
    public void applyToJob(Long jobId, String studentEmail) {

        // Find the student
        User student = userRepo.findByEmail(studentEmail)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        // Find the job
        Job job = jobRepo.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        // Prevent duplicate applications
        boolean exists = applicationRepo.existsByJob_JobIdAndStudent_UserId(
                jobId, student.getUserId());

        if (exists) {
            throw new RuntimeException("You already applied to this job");
        }

        // Create application
        JobApplication application = new JobApplication();
        application.setJob(job);
        application.setStudent(student);
        application.setStatus(JobApplication.ApplicationStatus.valueOf("SUBMITTED"));

        // Save
        applicationRepo.save(application);
    }
}
