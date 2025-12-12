package com.example.CampusJobBoard.Controllers;

import com.example.CampusJobBoard.Services.ApplicationService;
import com.example.CampusJobBoard.Services.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.example.CampusJobBoard.Services.JobService;

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
    public String home() {
        return "redirect:/student/dashboard";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {

        String email = "student@test.com"; // temporary

        model.addAttribute("studentName", "Student User");
        model.addAttribute("totalJobs", jobService.getApprovedJobs().size());
        model.addAttribute("totalApps", studentService.getMyApplications(email).size());

        return "student-dashboard";  // MUST match your file name exactly
    }

    @GetMapping("/jobs")
    public String viewApprovedJobs(Model model) {
        model.addAttribute("jobs", jobService.getApprovedJobs());
        return "job-list-student"; // correct
    }

    @GetMapping("/jobs/{id}")
    public String jobDetails(@PathVariable("id") Long id, Model model) {
        model.addAttribute("job", jobService.getJobById(id));
        return "job-details-student"; // correct
    }

    @PostMapping("/jobs/{id}/apply")
    public String apply(@PathVariable("id") Long id) {

        String email = "student@test.com"; // temporary

        applicationService.applyToJob(id, email);

        return "redirect:/student/jobs";
    }

    @GetMapping("/applications")
    public String myApps(Model model) {

        String email = "student@test.com";

        model.addAttribute("apps", studentService.getMyApplications(email));

        return "my-applications";  // correct
    }
}
