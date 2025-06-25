package com.practiceproject.linkchat_back.controller;

import com.practiceproject.linkchat_back.dtos.EmailRequest;
import com.practiceproject.linkchat_back.services.NotificationEmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class NotificationEmailController {

    private final NotificationEmailService notificationEmailService;

    @Autowired
    public NotificationEmailController(NotificationEmailService notificationEmailService) {
        this.notificationEmailService = notificationEmailService;
    }

    @PostMapping("/api/sendNotificationEmail")
    public void sendNotificationEmail(@RequestBody EmailRequest request) {
        try {
            notificationEmailService.sendNotificationEmail(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
