package com.practiceproject.linkchat_back.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class DashboardController {

    private String message = "";

    @GetMapping("/ui/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("message", message);
        return "dashboard";
    }

    @PostMapping("/ui/dashboard")
    public String dashboard(@RequestParam String newMessage) {
        this.message = newMessage;
        return "redirect:/ui/dashboard";
    }
}
