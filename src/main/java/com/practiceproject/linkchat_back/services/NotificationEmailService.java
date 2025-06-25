package com.practiceproject.linkchat_back.services;

import com.practiceproject.linkchat_back.dtos.EmailRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Service
public class NotificationEmailService {
    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    @Autowired
    public NotificationEmailService(JavaMailSender mailSender, TemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

    public void sendNotificationEmail(EmailRequest request) throws Exception {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, StandardCharsets.UTF_8.name());
        Context context = new Context();

        // Get template variables or use empty map if not provided
        Map<String, String> variables = request.getTemplateVariables() != null ?
                                    request.getTemplateVariables() :
                                    Map.of();

        // Set standard variables from request or use defaults from variables map
        context.setVariable("username", variables.getOrDefault("username", "User"));
        context.setVariable("notificationTitle", request.getSubject());
        context.setVariable("notificationMessage", request.getText());
        context.setVariable("actionLink", variables.getOrDefault("actionLink", "#"));
        context.setVariable("from", variables.getOrDefault("from", "System"));

        // Add any additional variables from the map
        for (Map.Entry<String, String> entry : variables.entrySet()) {
            context.setVariable(entry.getKey(), entry.getValue());
        }

        String html = templateEngine.process("notification-email", context);
        helper.setTo(request.getTo());
        helper.setSubject(request.getSubject());
        helper.setText(html, true);
        mailSender.send(message);
    }
}
