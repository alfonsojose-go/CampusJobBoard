package com.example.CampusJobBoard.Controllers;

import com.example.CampusJobBoard.Models.Job;
import com.example.CampusJobBoard.Models.JobApplication;
import com.example.CampusJobBoard.Models.User;
import com.example.CampusJobBoard.Repositories.JobApplicationRepo;
import com.example.CampusJobBoard.Repositories.JobRepo;
import com.example.CampusJobBoard.Repositories.UserRepo;
import com.example.CampusJobBoard.Services.JobApplicationService;
import com.example.CampusJobBoard.Services.JobService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

@Controller
@RequestMapping("/job")
public class JobController {

    private final JobService jobService;
    private final JobRepo jobRepo;
    private final JobApplicationService jobApplicationService;
    private final JobApplicationRepo jobApplicationRepo;
    private final UserRepo userRepo;

    public JobController(JobService jobService, JobRepo jobRepo,
                         JobApplicationService jobApplicationService,
                         JobApplicationRepo jobApplicationRepo,
                         UserRepo userRepo) {
        this.jobService = jobService;
        this.jobRepo = jobRepo;
        this.jobApplicationService = jobApplicationService;
        this.jobApplicationRepo = jobApplicationRepo;
        this.userRepo = userRepo;
    }

    // Helper method to get current logged-in user
    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof UserDetails) {
                String username = ((UserDetails) principal).getUsername();
                return userRepo.findByEmail(username)
                        .orElseThrow(() -> new RuntimeException("User not found with username: " + username));
            } else if (principal instanceof String) {
                // Handle String principal (like username)
                String username = (String) principal;
                return userRepo.findByEmail(username)
                        .orElseThrow(() -> new RuntimeException("User not found with username: " + username));
            }
        }
        throw new RuntimeException("No authenticated user found");
    }

    @GetMapping
    public String listJobs(Model model) {
        // Get jobs for current employer only
        User currentUser = getCurrentUser();
        List<Job> jobs = jobService.findByEmployer(currentUser);
        model.addAttribute("jobs", jobs);
        return "job-list";
    }

    @GetMapping("/new")
    public String showForm(Model model) {
        model.addAttribute("jobs", new Job());
        return "job-form";
    }

    @PostMapping("/save")
    public String saveJob(@ModelAttribute("job") Job job, BindingResult result) {
        if (result.hasErrors()) {
            return "job-form";
        }

        // Get current logged-in employer
        User currentEmployer = getCurrentUser();

        // Set the employer to current user
        job.setEmployer(currentEmployer);

        // Set status
        job.setStatus(Job.JobStatus.PENDING);

        jobService.save(job);
        return "redirect:/job";
    }

    // DELETE ITEM
    @GetMapping("/delete/{jobId}")
    public String deleteJob(@PathVariable("jobId") Long jobId) {
        // Optional: Add authorization check to ensure user owns this job
        Job job = jobService.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        User currentUser = getCurrentUser();
        if (job.getEmployer().getUserId() != currentUser.getUserId()) {
            throw new RuntimeException("You are not authorized to delete this job");
        }

        jobService.deleteById(jobId);
        return "redirect:/job";
    }

    // UPDATE ITEM
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        Job job = jobService.findById(id)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        // Check if current user owns this job
        User currentUser = getCurrentUser();
        if (job.getEmployer().getUserId() != currentUser.getUserId()) {
            throw new RuntimeException("You are not authorized to edit this job");
        }

        model.addAttribute("jobs", job);
        return "job-form";
    }

    //Get job applications for current employer's jobs
    @GetMapping("/job-applications")
    public String getJobsWithSubmittedApplications(Model model) {
        User currentEmployer = getCurrentUser();

        // Get applications for current employer's jobs only
        List<JobApplication> jobApplications = jobApplicationService.getApplicationsForEmployer(currentEmployer);

        model.addAttribute("jobApplications", jobApplications);
        return "job-applications";
    }

    // Accept job application
    @GetMapping("/accept/{applicationId}")
    public String acceptJobApplication(@PathVariable("applicationId") Long applicationId) {
        JobApplication jobApplication = jobApplicationRepo.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Job application not found"));

        // Check if current user is the employer for this job
        User currentUser = getCurrentUser();
        if (jobApplication.getJob().getEmployer().getUserId() != currentUser.getUserId()) {
            throw new RuntimeException("You are not authorized to update this application");
        }

        jobApplication.setStatus(JobApplication.ApplicationStatus.ACCEPTED);
        jobApplicationRepo.save(jobApplication);
        return "redirect:/job/job-applications";
    }

    // Reject job application
    @GetMapping("/reject/{applicationId}")
    public String rejectJobApplication(@PathVariable("applicationId") Long applicationId) {
        JobApplication jobApplication = jobApplicationRepo.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Job application not found"));

        // Check if current user is the employer for this job
        User currentUser = getCurrentUser();
        if (jobApplication.getJob().getEmployer().getUserId() != currentUser.getUserId()) {
            throw new RuntimeException("You are not authorized to update this application");
        }

        jobApplication.setStatus(JobApplication.ApplicationStatus.REJECTED);
        jobApplicationRepo.save(jobApplication);
        return "redirect:/job/job-applications";
    }
}