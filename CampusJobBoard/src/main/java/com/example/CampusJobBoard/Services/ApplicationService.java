package com.example.CampusJobBoard.Services;

import com.example.CampusJobBoard.Models.Job;
import com.example.CampusJobBoard.Models.JobApplication;
import com.example.CampusJobBoard.Models.User;
import com.example.CampusJobBoard.Repositories.JobApplicationRepo;
import com.example.CampusJobBoard.Repositories.JobRepo;
import com.example.CampusJobBoard.Repositories.UserRepo;
import org.springframework.stereotype.Service;

@Service
public class ApplicationService {

    private final JobApplicationRepo applicationRepo;
    private final JobRepo jobRepo;
    private final UserRepo userRepo;

    public ApplicationService(JobApplicationRepo applicationRepo,
                              JobRepo jobRepo,
                              UserRepo userRepo) {
        this.applicationRepo = applicationRepo;
        this.jobRepo = jobRepo;
        this.userRepo = userRepo;
    }

    // -------------------------------------------------
    // APPLY TO JOB (SAFE VERSION)
    // -------------------------------------------------
    public void applyToJob(Long jobId, String studentEmail) {

        // Find student
        User student = userRepo.findByEmail(studentEmail)
                .orElse(null);

        if (student == null) {
            return; // fail silently (safe for assignment)
        }

        // Find job
        Job job = jobRepo.findById(jobId)
                .orElse(null);

        if (job == null) {
            return;
        }

        // Prevent duplicate applications
        boolean alreadyApplied =
                applicationRepo.existsByJob_JobIdAndStudent_UserId(
                        jobId,
                        student.getUserId()
                );

        if (alreadyApplied) {
            return; // already applied → do nothing
        }

        // Create application
        JobApplication application = new JobApplication();
        application.setJob(job);
        application.setStudent(student);
        application.setStatus(JobApplication.ApplicationStatus.SUBMITTED);

        applicationRepo.save(application);
    }

    // -------------------------------------------------
    // CHECK IF STUDENT ALREADY APPLIED
    // -------------------------------------------------
    public boolean hasStudentApplied(Long jobId, String studentEmail) {

        User student = userRepo.findByEmail(studentEmail)
                .orElse(null);

        if (student == null) {
            return false;
        }

        return applicationRepo.existsByJob_JobIdAndStudent_UserId(
                jobId,
                student.getUserId()
        );
    }
}