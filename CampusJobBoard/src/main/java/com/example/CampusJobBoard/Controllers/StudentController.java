package com.example.CampusJobBoard.Controllers;

import com.example.CampusJobBoard.Services.ApplicationService;
import com.example.CampusJobBoard.Services.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.example.CampusJobBoard.Services.JobService;


import java.security.Principal;

@Controller
@RequestMapping("/student")
public class StudentController {

    private final JobService jobService;
    private final ApplicationService applicationService;
    private final StudentService studentService;

    @Autowired
    public StudentController(JobService jobService,
                             ApplicationService applicationService,
                             StudentService studentService) {
        this.jobService = jobService;
        this.applicationService = applicationService;
        this.studentService = studentService;
    }

    @GetMapping({"", "/"})
    public String studentHome() {
        return "redirect:/student/jobs";
    }

    @GetMapping("/jobs")
    public String viewApprovedJobs(Model model) {
        model.addAttribute("jobs", jobService.getApprovedJobs());
        return "job-list-student";
    }

    @GetMapping("/jobs/{id}")
    public String jobDetails(@PathVariable Long id, Model model) {
        model.addAttribute("job", jobService.getJobById(id));
        return "job-details-student";
    }

    @PostMapping("/jobs/{id}/apply")
    public String apply(@PathVariable Long id, Principal principal) {
        applicationService.applyToJob(id, principal.getName());
        return "redirect:/student/jobs";
    }

    @GetMapping("/applications")
    public String myApps(Model model, Principal principal) {
        model.addAttribute("apps", studentService.getMyApplications(principal.getName()));
        return "my-applications";
    }
}
