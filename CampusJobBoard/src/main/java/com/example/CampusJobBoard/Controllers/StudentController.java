package com.example.CampusJobBoard.Controllers;

import com.example.CampusJobBoard.Services.ApplicationService;
import com.example.CampusJobBoard.Services.JobService;
import com.example.CampusJobBoard.Services.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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

    // ---------------- DASHBOARD ----------------
    @GetMapping("/dashboard")
    public String dashboard(Model model, Principal principal) {

        String email = principal.getName(); // 🔥 REAL LOGGED-IN USER

        model.addAttribute("studentName", email);
        model.addAttribute("totalJobs", jobService.getApprovedJobs().size());
        model.addAttribute("totalApps", studentService.getMyApplications(email).size());

        return "student-dashboard";
    }

    // ---------------- JOB LIST ----------------
    @GetMapping("/jobs")
    public String viewApprovedJobs(Model model) {
        model.addAttribute("jobs", jobService.getApprovedJobs());
        return "job-list-student";
    }

    // ---------------- JOB DETAILS ----------------
    @GetMapping("/jobs/{id}")
    public String jobDetails(@PathVariable("id") Long id,
                             Model model,
                             Principal principal) {

        String email = principal.getName();

        model.addAttribute("job", jobService.getJobById(id));

        boolean alreadyApplied =
                applicationService.hasStudentApplied(id, email);

        model.addAttribute("alreadyApplied", alreadyApplied);

        return "job-details-student";
    }


    // ---------------- APPLY TO JOB ----------------
    @PostMapping("/jobs/{id}/apply")
    public String apply(@PathVariable Long id, Principal principal) {

        String email = principal.getName(); // 🔥 REAL USER

        applicationService.applyToJob(id, email);

        return "redirect:/student/applications";
    }

    // ---------------- MY APPLICATIONS ----------------
    @GetMapping("/applications")
    public String myApplications(Model model, Principal principal) {

        String email = principal.getName(); // 🔥 REAL USER

        model.addAttribute("apps", studentService.getMyApplications(email));

        return "my-applications";
    }
}
