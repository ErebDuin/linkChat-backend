package com.practiceproject.linkchat_back.controller;

import com.practiceproject.linkchat_back.dtos.ChatMessageResponse;
import com.practiceproject.linkchat_back.dtos.ImageMessageRequest;
import com.practiceproject.linkchat_back.model.ChatMessage;
import com.practiceproject.linkchat_back.services.ChatMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/messages")
@CrossOrigin(origins = "*")
public class ChatMessageApiController {

    @Autowired
    private ChatMessageService chatMessageService;

    @PostMapping("/text")
    public ResponseEntity<ChatMessageResponse> sendTextMessage(@RequestBody Map<String, Object> request) {
        try {
            String sender = (String) request.get("sender");
            String recipient = (String) request.get("recipient");
            String messageText = (String) request.get("messageText");
            Long chatId = request.get("chatId") != null ?
                Long.valueOf(request.get("chatId").toString()) : null;

            if (sender == null || recipient == null || messageText == null) {
                return ResponseEntity.badRequest().build();
            }

            ChatMessage message = chatMessageService.sendTextMessage(sender, recipient, chatId, messageText);
            return ResponseEntity.ok(new ChatMessageResponse(message));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/image")
    public ResponseEntity<ChatMessageResponse> sendImageMessage(@RequestBody ImageMessageRequest request) {
        try {
            if (request.getSender() == null || request.getRecipient() == null ||
                request.getImageBase64() == null || request.getImageBase64().isEmpty()) {
                return ResponseEntity.badRequest().build();
            }

            ChatMessage message = chatMessageService.sendImageMessage(request);
            return ResponseEntity.ok(new ChatMessageResponse(message));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{messageId}/image")
    public ResponseEntity<String> getImageData(@PathVariable Long messageId) {
        try {
            ChatMessage message = chatMessageService.findById(messageId);
            if (message == null || message.getMessageType() != ChatMessage.MessageType.IMAGE) {
                return ResponseEntity.notFound().build();
            }

            if (message.getImageData() != null) {
                String base64Image = java.util.Base64.getEncoder().encodeToString(message.getImageData());
                return ResponseEntity.ok(base64Image);
            }

            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
