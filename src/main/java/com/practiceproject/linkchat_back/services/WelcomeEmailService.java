package com.practiceproject.linkchat_back.services;

import java.nio.charset.StandardCharsets;
import java.util.Map;

import org.springframework.mail.javamail.MimeMessageHelper;
import org.thymeleaf.context.Context;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import com.practiceproject.linkchat_back.dtos.EmailRequest;

import jakarta.mail.internet.MimeMessage;

@Service
public class WelcomeEmailService {
    
    private final NotificationEmailService notificationEmailService;

    public WelcomeEmailService(NotificationEmailService notificationEmailService) {
        this.notificationEmailService = notificationEmailService;
    }

    public void sendWelcomeEmail(EmailRequest request) throws Exception {
        //MimeMessage message = mailSender.createMimeMessage();
        //MimeMessageHelper helper = new MimeMessageHelper(message, true, StandardCharsets.UTF_8.name());
        Context context = new Context();

        // Get template variables or use empty map if not provided
        Map<String, String> variables = request.getTemplateVariables() != null ?
                                    request.getTemplateVariables() :
                                    Map.of();

        // Set standard variables from request or use defaults from variables map
        context.setVariable("username", variables.getOrDefault("username", "User"));
        context.setVariable("from", variables.getOrDefault("from", "System"));

        // Add any additional variables from the map
        for (Map.Entry<String, String> entry : variables.entrySet()) {
            context.setVariable(entry.getKey(), entry.getValue());
        }

        //String html = templateEngine.process("welcome-email", context);
        //helper.setTo(request.getTo());
        //helper.setSubject(request.getSubject());
        //helper.setText(html, true);
        //mailSender.send(message);
    }       
}
