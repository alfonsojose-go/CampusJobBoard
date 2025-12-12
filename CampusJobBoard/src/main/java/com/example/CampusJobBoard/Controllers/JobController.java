package com.example.CampusJobBoard.Controllers;


import com.example.CampusJobBoard.Models.Job;
import com.example.CampusJobBoard.Models.JobApplication;
import com.example.CampusJobBoard.Models.User;
import com.example.CampusJobBoard.Repositories.JobApplicationRepo;
import com.example.CampusJobBoard.Repositories.JobRepo;
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

import java.util.List;

@Controller
@RequestMapping("/job")
public class JobController {

    private final JobService jobService;
    private final JobRepo jobRepo;
    private final JobApplicationService jobApplicationService;
    private final JobApplicationRepo jobApplicationRepo;

    public JobController(JobService jobService, JobRepo jobRepo, JobApplicationService jobApplicationService, JobApplicationRepo jobApplicationRepo) {
        this.jobService = jobService;
        this.jobRepo = jobRepo;
        this.jobApplicationService = jobApplicationService;
        this.jobApplicationRepo = jobApplicationRepo;
    }


    @GetMapping
    public String listJobs(Model model){
        model.addAttribute("jobs", jobService.findAll());
        return "job-list";
    }

    @GetMapping("/new")
    public String showForm(Model model){
        model.addAttribute("jobs",new Job());
        return "job-form";
    }

    @PostMapping("/save")
    public String saveJob(@ModelAttribute("job") Job job, BindingResult result ){
        if (result.hasErrors()){
            return "job-form";
        }
        // Fetch the User entity with ID=1 (the employer)
        // update this after Adarsh's code
//        User employer = userRepository.findById(1L)
//                .orElseThrow(() -> new RuntimeException("User (employer) not found with ID: 1"));


        //use this code for now
        User tempEmployer = new User();
        tempEmployer.setUserId(1L);

        // Set the employer (User entity)
        job.setEmployer(tempEmployer);

        // Set status
        job.setStatus(Job.JobStatus.PENDING);

        jobService.save(job);
        return "redirect:/job";

    }

    // DELETE ITEM
    @GetMapping("/delete/{jobId}")
    public String deleteJob(@PathVariable("jobId") Long jobId) {
        jobService.deleteById(jobId);
        return "redirect:/job";
    }

    // UPDATE ITEM
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        Job job = jobService.findById(id)
                .orElseThrow(() -> new RuntimeException("Job not found"));
        model.addAttribute("jobs", job);
        return "job-form"; // Same template!
    }

    //Get job applications
    @GetMapping("/job-applications")
    public String getJobsWithSubmittedApplications(Model model) {
        List<JobApplication> jobApplication = jobApplicationService.getJobsWithSubmittedApplications();

        // Add jobs to the model for Thymeleaf
        model.addAttribute("jobApplications", jobApplication);

         // Return the name of the Thymeleaf template
        return "job-applications";
    }

    // Accept job application
    @GetMapping("/accept/{applicationId}")
    public String acceptJobApplication(@PathVariable("applicationId") Long applicationId) {
        JobApplication jobApplication = jobApplicationRepo.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Job application not found"));

        jobApplication.setStatus(JobApplication.ApplicationStatus.ACCEPTED);
        jobApplicationRepo.save(jobApplication);
        return "redirect:/job/job-applications";
    }

    // reject job application
    @GetMapping("/reject/{applicationId}")
    public String rejectJobApplication(@PathVariable("applicationId") Long applicationId) {
        JobApplication jobApplication = jobApplicationRepo.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Job application not found"));

        jobApplication.setStatus(JobApplication.ApplicationStatus.REJECTED);
        jobApplicationRepo.save(jobApplication);
        return "redirect:/job/job-applications"; // Same template!
    }
}
