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
}
