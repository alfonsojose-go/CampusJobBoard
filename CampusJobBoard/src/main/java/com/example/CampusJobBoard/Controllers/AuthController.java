package com.example.CampusJobBoard.Controllers;

import com.example.CampusJobBoard.Services.UserService;
import com.example.CampusJobBoard.dto.UserRegistrationDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("user", new UserRegistrationDto()); // IMPORTANT
        return "register";
    }

    @PostMapping("/register")
    public String registerSubmit(@ModelAttribute("user") UserRegistrationDto dto, Model model) {
        try {
            userService.registerNewUser(dto);
            return "redirect:/login?registered";
        } catch (IllegalArgumentException ex) {
            model.addAttribute("error", ex.getMessage());   // show message in the page
            model.addAttribute("user", dto);                // keep the filled form data
            return "register";
        }
    }

}
