package com.practiceproject.linkchat_back.controller;

import com.practiceproject.linkchat_back.adminLoginPageController.AdminLoginPageController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class WelcomeEmailController {
    private static final Logger logger = LoggerFactory.getLogger(AdminLoginPageController.class);

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private SpringTemplateEngine templateEngine;

    @GetMapping("/ui/email")
    public String email() {
        logger.debug("Accessing welcome email page");
        return "welcome-email";
    }

    @PostMapping("/ui/email")
    @ResponseBody
    public String sendWelcomeEmail(
            @RequestParam String to,
            @RequestParam String userName,
            @RequestParam String platformName,
            @RequestParam String emailSignature
    ) {
        logger.debug("Sending email");
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