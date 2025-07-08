package com.practiceproject.linkchat_back.controller;

import com.practiceproject.linkchat_back.dtos.EmailRequest;
import com.practiceproject.linkchat_back.services.NotificationEmailService;
import com.practiceproject.linkchat_back.services.WelcomeEmailService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class WelcomeEmailController {

    private final WelcomeEmailService welcomeEmailService;

    @Autowired
    public WelcomeEmailController(WelcomeEmailService welcomeEmailService) {
        this.welcomeEmailService = welcomeEmailService;
    }

    @PostMapping("/api/sendWelcomeEmail")
    public void sendWelcomeEmail(@RequestBody EmailRequest request) {
        try {
            welcomeEmailService.sendWelcomeEmail(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}