package com.practiceproject.linkchat_back.controller;

import com.practiceproject.linkchat_back.services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import com.practiceproject.linkchat_back.dtos.EmailRequest;
import org.springframework.web.bind.annotation.*;

@RestController
public class EmailController {

    private final EmailService emailService;

    @Autowired
    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping("/api/sendEmailUrl")
    public void sendEmailUrl(@RequestParam String to,
                          @RequestParam String subject,
                          @RequestParam String text) {
        emailService.sendSimpleMessage(to, subject, text);
    }

    @PostMapping("/api/sendEmailJson")
    public void sendEmailJson(@RequestBody EmailRequest request) {
        emailService.sendSimpleMessage(request.getTo(), request.getSubject(), request.getText());
    }
}
