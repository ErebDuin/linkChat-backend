package com.practiceproject.linkchat_back.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/chat")
public class ChatController {
    private static final Logger logger = LoggerFactory.getLogger(ChatController.class);

    @GetMapping
    public Map<String, Object> getChatData() {
        logger.debug("Fetching chat data");
        return Map.of(
                "title", "This is our chat",
                "users", List.of(
                        Map.of("name", "Eugen", "status", "online"),
                        Map.of("name", "John", "status", "offline")
                ),
                "messages", List.of(
                        Map.of("sender", "Eugen", "content", "Hello everyone!", "timestamp", "2023-10-01T12:00:00"),
                        Map.of("sender", "John", "content", "Hi Eugen!", "timestamp", "2023-10-01T12:01:00")
                ),
                "settings", Map.of("createdAt", "2023-10-01T12:00:00")
        );
    }
}
