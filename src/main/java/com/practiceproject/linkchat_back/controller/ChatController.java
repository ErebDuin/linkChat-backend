package com.practiceproject.linkchat_back.controller;
import com.practiceproject.linkchat_back.model.ChatInfo;
import com.practiceproject.linkchat_back.repository.ChatMessageRepository;
import com.practiceproject.linkchat_back.repository.ChatRepository;
import com.practiceproject.linkchat_back.repository.ChatUserRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import com.practiceproject.linkchat_back.model.Chat;
import com.practiceproject.linkchat_back.model.ChatMessage;
@RestController
@RequestMapping("/api/chat")
@Tag(name = "Chat API", description = "Operations related to chat data")
public class ChatController {
    private static final Logger logger = LoggerFactory.getLogger(ChatController.class);
    private final ChatRepository chatRepository;
    private final ChatUserRepository chatUserRepository;
    private final ChatMessageRepository chatMessageRepository;

    public ChatController(ChatRepository chatRepository,
                          ChatUserRepository chatUserRepository,
                          ChatMessageRepository chatMessageRepository) {
        this.chatRepository = chatRepository;
        this.chatUserRepository = chatUserRepository;
        this.chatMessageRepository = chatMessageRepository;
    }

    @Operation(summary = "Get chat data by chat link")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Chat data retrieved")
    })
    @GetMapping("/{link}")
    public ChatInfo getChatData(@PathVariable("link") String link) {
        logger.debug("Fetching chat data for {}", link);
        return new ChatInfo(link, chatRepository, chatUserRepository, chatMessageRepository);

    }
    @PostMapping("/{link}/message")
    public void sendMessage(@PathVariable("link") String link, @RequestBody ChatMessage message) {
        Chat chat = chatRepository.findByLink(link).orElse(null);
        if (chat == null) throw new RuntimeException("Chat not found");
        message.setChat(chat);
        chatMessageRepository.save(message);
    }
    @PostMapping
    public Chat createChat(@RequestBody Chat chat) {
        // Generate a random 6-character string
        String link = generateRandomLink(6);
        chat.setLink(link);
        return chatRepository.save(chat);
    }

    private String generateRandomLink(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder();
        java.util.Random random = new java.util.Random();
        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }

}
