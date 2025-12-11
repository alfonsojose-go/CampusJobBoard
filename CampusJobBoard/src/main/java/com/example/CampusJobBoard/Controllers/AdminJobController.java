package com.example.CampusJobBoard.Controllers;

import com.example.CampusJobBoard.Models.Job;
import com.example.CampusJobBoard.Repositories.JobRepo;
import com.example.CampusJobBoard.Services.JobService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminJobController {
    private final JobService jobService;
    private final JobRepo jobRepo;

    public AdminJobController(JobService jobService, JobRepo jobRepo) {
        this.jobService = jobService;
        this.jobRepo = jobRepo;
    }

    @GetMapping("/job-review")
    public String reviewJobs(Model model){
        model.addAttribute("jobs", jobService.findAll());
        return "job-review";
    }


    //APPROVE ITEM
    @GetMapping("/approve/{jobId}")
    public String approveJob(@PathVariable Long jobId) {
        Job job = jobService.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        job.setStatus(Job.JobStatus.APPROVED);
        jobRepo.save(job);

        return "redirect:/admin/job-review";
    }

    //APPROVE ITEM
    @GetMapping("/reject/{jobId}")
    public String rejectJob(@PathVariable Long jobId) {
        Job job = jobService.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        job.setStatus(Job.JobStatus.REJECTED);
        jobRepo.save(job);

        return "redirect:/admin/job-review";

    }

}
