package com.example.CampusJobBoard.Controllers;


import com.example.CampusJobBoard.Models.Job;
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


    @GetMapping("/test")
    public String test() {
        return "This is a test!";  // Direct string response
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
        jobService.save(job);
        return "redirect:/job";

    }
}
