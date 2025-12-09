package com.example.CampusJobBoard.Controllers;


import com.example.CampusJobBoard.Models.Job;
import com.example.CampusJobBoard.Models.User;
import com.example.CampusJobBoard.Repositories.JobRepo;
import com.example.CampusJobBoard.Services.JobService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/job")
public class JobController {

    private final JobService jobService;
    private final JobRepo jobRepo;

    public JobController(JobService jobService, JobRepo jobRepo) {
        this.jobService = jobService;
        this.jobRepo = jobRepo;
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
    public String deleteJob(@PathVariable Long jobId) {
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
}
