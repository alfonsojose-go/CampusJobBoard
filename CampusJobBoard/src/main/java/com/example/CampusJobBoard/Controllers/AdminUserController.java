package com.example.CampusJobBoard.Controllers;

import com.example.CampusJobBoard.Models.User;
import com.example.CampusJobBoard.Repositories.UserRepo;
import com.example.CampusJobBoard.Services.UserMgtService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminUserController {

    private final UserRepo userRepo;
    private final UserMgtService service;

    public AdminUserController(UserRepo userRepo, UserMgtService service) {
        this.userRepo = userRepo;
        this.service = service;
    }



    @GetMapping("/users")
    public String reviewJobs(Model model){
        model.addAttribute("users", service.findAll());
        return "user-mgt";
    }

    //APPROVE ITEM
    @GetMapping("/activate/{jobId}")
    public String approveJob(@PathVariable Long jobId) {
        User user = service.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        user.setStatus(User.UserStatus.ACTIVE);
        userRepo.save(user);

        return "redirect:/admin/users";
    }

    //REJECT ITEM
    @GetMapping("/deactivate/{jobId}")
    public String rejectJob(@PathVariable Long jobId) {
        User user = service.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        user.setStatus(User.UserStatus.INACTIVE);
        userRepo.save(user);

        return "redirect:/admin/users";

    }

}
