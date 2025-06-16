package com.practiceproject.linkchat_back.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Controller
@RequestMapping("/email")
public class WelcomeEmailController {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private SpringTemplateEngine templateEngine;

    @PostMapping("/send-welcome")
    @ResponseBody
    public String sendWelcomeEmail(
            @RequestParam String to,
            @RequestParam String userName,
            @RequestParam String platformName,
            @RequestParam String emailSignature
    ) {
        try {
            // Prepare the Thymeleaf context with variables
            Context context = new Context();
            context.setVariable("userName", userName);
            context.setVariable("platformName", platformName);
            context.setVariable("emailSignature", emailSignature);

            // Process the template
            String htmlContent = templateEngine.process("welcome-email.html", context);

            // Prepare and send the email
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(to);
            helper.setSubject("Welcome to " + platformName);
            helper.setText(htmlContent, true);

            mailSender.send(message);

            return "Welcome email sent successfully!";
        } catch (MessagingException e) {
            return "Failed to send welcome email: " + e.getMessage();
        }
    }
}