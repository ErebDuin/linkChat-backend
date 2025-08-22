package com.practiceproject.linkchat_back.controller;

import com.practiceproject.linkchat_back.dtos.ChatMessageRequest;
import com.practiceproject.linkchat_back.dtos.ChatMessageResponse;
import com.practiceproject.linkchat_back.dtos.ImageMessageRequest;
import com.practiceproject.linkchat_back.model.ChatMessage;
import com.practiceproject.linkchat_back.services.ChatMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/messages")
@CrossOrigin(origins = "*")
public class ChatMessageApiController {

    private static final Logger logger = LoggerFactory.getLogger(ChatMessageApiController.class);

    @Autowired
    private ChatMessageService chatMessageService;

    @PostMapping("/text")
    public ResponseEntity<ChatMessageResponse> sendTextMessage(@RequestBody ChatMessageRequest request) {
        try {
            ChatMessage saved = chatMessageService.sendTextMessage(request);
            return ResponseEntity.ok(ChatMessageResponse.from(saved));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/image")
    public ResponseEntity<ChatMessageResponse> sendImageMessage(@RequestBody ImageMessageRequest request) {
        try {
            logger.info("Attempting to send image message via API");
            ChatMessage message = chatMessageService.sendImageMessage(request);
            logger.info("Successfully saved image message via API with ID: {}", message.getMessageId());

            return ResponseEntity.ok(new ChatMessageResponse(message));
        } catch (RuntimeException e) {
            logger.error("Runtime error sending image message via API - Error: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            logger.error("Unexpected error sending image message via API - Error: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{messageId}/image")
    public ResponseEntity<String> getImageData(@PathVariable Long messageId) {
        try {
            logger.info("Attempting to retrieve image data for message ID: {}", messageId);

            ChatMessage message = chatMessageService.findById(messageId);


            if (message.getImageData() != null) {
                logger.info("Successfully retrieved image data for message ID: {}, data length: {}",
                        messageId, message.getImageData().length());
                return ResponseEntity.ok(message.getImageData());
            }

            logger.error("Image data is null for message ID: {}", messageId);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.error("Error retrieving image data for message ID: {} - Error: {}", messageId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}

