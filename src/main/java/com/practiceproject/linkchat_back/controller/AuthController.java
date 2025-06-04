package com.practiceproject.linkchat_back.controller;

import com.practiceproject.linkchat_back.adminLoginPageController.AdminLoginPageController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class AuthController {
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @PostMapping("/api/auth")
    public Map<String, Object> auth(@RequestBody Map<String, String> payload) {
        logger.debug("Received authentication request with payload: {}", payload);
        String username = payload.get("username");
        String password = payload.get("password");

        Map<String, Object> response = new HashMap<>();

        if ("admin".equals(username) && "password123".equals(password)) {
            response.put("message", "Login successful!");
        } else {
            response.put("message", "Invalid username or password.");
        }

        return response;
    }
}
