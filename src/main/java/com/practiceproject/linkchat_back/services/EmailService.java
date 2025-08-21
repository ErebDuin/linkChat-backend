package com.practiceproject.linkchat_back.services;

import com.practiceproject.linkchat_back.dtos.EmailRequest;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Map;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender emailSender;

    public void sendSimpleMessage(String to, String subject, String text) throws MessagingException {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper mimeMessage = new MimeMessageHelper(message, true, "UTF-8");
        mimeMessage.setFrom("sysportnov@gmail.com");
        mimeMessage.setTo(to);
        mimeMessage.setSubject(subject);
        mimeMessage.setText(text, true);
        emailSender.send(message);
    }

    public void sendWithTemplate(EmailRequest request) throws Exception {
        ClassPathResource resource = new ClassPathResource("templates/" + request.getTemplateName() + ".html");
        String templateContent = new String(Files.readAllBytes(resource.getFile().toPath()), StandardCharsets.UTF_8);

        for (Map.Entry<String, String> entry : request.getTemplateVariables().entrySet()) {
            templateContent = templateContent.replace("{{" + entry.getKey() + "}}", entry.getValue());
        }

        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setTo(request.getTo());
        helper.setSubject(request.getSubject());
        helper.setText(templateContent, true);

        emailSender.send(message);
    }

    public void sendInvite(String to, String username, String link, int ttlHours) throws Exception {

        ClassPathResource res = new ClassPathResource("templates/inviteToTheChat.html");
        String html = Files.readString(res.getFile().toPath(), StandardCharsets.UTF_8);

        html = html.replace("{{username}}", username)
                .replace("{{sender}}",   "sysportnov@gmail.com")
                .replace("{{link}}",     link)
                .replace("{{ttlHours}}", String.valueOf(ttlHours))
                .replace("{{inviteId}}", java.util.UUID.randomUUID().toString());

        MimeMessage msg = emailSender.createMimeMessage();
        MimeMessageHelper h = new MimeMessageHelper(msg, true, "UTF-8");
        h.setFrom("sysportnov@gmail.com");
        h.setTo(to);
        h.setSubject("You’ve got a chat invite!");
        h.setText(html, true);

        emailSender.send(msg);
    }
    public void sendChatTitleUpdatedEmail(String to, String chatTitle, String link) throws Exception {
        ClassPathResource res = new ClassPathResource("templates/chatTitleUpdated.html");
        String html = Files.readString(res.getFile().toPath(), StandardCharsets.UTF_8);

        html = html.replace("{{chatTitle}}", chatTitle)
                .replace("{{link}}", link);

        MimeMessage msg = emailSender.createMimeMessage();
        MimeMessageHelper h = new MimeMessageHelper(msg, true, "UTF-8");
        h.setFrom("sysportnov@gmail.com");
        h.setTo(to);
        h.setSubject("Chat Title Updated");
        h.setText(html, true);

        emailSender.send(msg);
    }
}
