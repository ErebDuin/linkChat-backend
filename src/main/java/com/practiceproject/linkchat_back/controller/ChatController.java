package com.practiceproject.linkchat_back.controller;
import com.practiceproject.linkchat_back.dtos.ChatSettingDtoApi;
import com.practiceproject.linkchat_back.dtos.MessageDto;
import com.practiceproject.linkchat_back.model.ChatInfo;
import com.practiceproject.linkchat_back.model.ChatSetting;
import com.practiceproject.linkchat_back.repository.ChatMessageRepository;
import com.practiceproject.linkchat_back.repository.ChatRepository;
import com.practiceproject.linkchat_back.repository.ChatSettingRepository;
import com.practiceproject.linkchat_back.repository.ChatUserRepository;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.practiceproject.linkchat_back.model.Chat;
import com.practiceproject.linkchat_back.model.ChatMessage;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/chat")
@Tag(name = "Chat API", description = "Operations related to chat data")
public class ChatController {
    private static final Logger logger = LoggerFactory.getLogger(ChatController.class);
    private final ChatRepository chatRepository;
    private final ChatUserRepository chatUserRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatSettingRepository chatSettingRepository;


    public ChatController(ChatRepository chatRepository,
                          ChatUserRepository chatUserRepository,
                          ChatMessageRepository chatMessageRepository,
                          ChatSettingRepository chatSettingRepository) {
        this.chatRepository = chatRepository;
        this.chatUserRepository = chatUserRepository;
        this.chatMessageRepository = chatMessageRepository;
        this.chatSettingRepository = chatSettingRepository;
    }

    @Operation(summary = "Get chat data by chat link")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Chat data retrieved")
    })
    @GetMapping("/{link}")
    public ChatInfo getChatData(@PathVariable("link") String link) {
        logger.debug("Fetching chat data for {}", link);
        return new ChatInfo(link, chatRepository, chatUserRepository, chatMessageRepository);

    }
    //    @PostMapping("/{link}/message")
//    public void sendMessage(@PathVariable("link") String link, @RequestBody ChatMessage message) {
//        Chat chat = chatRepository.findByLink(link).orElse(null);
//        if (chat == null) throw new RuntimeException("Chat not found");
//        message.setChat(chat);
//        chatMessageRepository.save(message);
//    }
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

    @PostMapping("/{link}/message")
    public ResponseEntity<ApiResponse> sendMessage(@PathVariable String link,
                                                   @Valid @RequestBody MessageDto dto,
                                                   BindingResult br) {

        if (br.hasErrors()) {
            return ResponseEntity.badRequest()
                    .body(new ChatController.ApiResponse("Invalid message payload"));
        }

        Optional<Chat> chatOpt = chatRepository.findByLink(link);
        if (chatOpt.isEmpty()) {
            logger.warn("Chat with link {} not found", link);
            return ResponseEntity.ok(new ChatController.ApiResponse("Message received, but chat not found"));
        }

        Chat chat = chatOpt.get();
        ChatMessage msg = new ChatMessage();
        msg.setMessageText(dto.messageText);
        msg.setSender(dto.sender);
        msg.setRecipient(dto.recipient);
        msg.setTimestamp(dto.timestamp);
        msg.setChat(chat);
        chatMessageRepository.save(msg);

        return ResponseEntity.ok(new ChatController.ApiResponse("Message sent"));
    }

    @Validated
    @GetMapping("/{chatId}/settings")
    public ResponseEntity<?> getSettings(@PathVariable Long chatId) {

        if (chatId <= 0) {
            return ResponseEntity.badRequest()
                    .body("chatId must be a positive number");
        }
        if (!chatRepository.existsById(chatId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Chat id " + chatId + " not found");
        }
        List<ChatSettingDtoApi> settings = chatSettingRepository.findByChatId(chatId)
                .stream()
                .map(s -> new ChatSettingDtoApi(s.getName(), s.getValue()))
                .toList();

        if (settings.isEmpty()) {
            return ResponseEntity.ok(new ApiResponse("No settings for this chat"));
        }
        return ResponseEntity.ok(("Settings fetched" + settings));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<String> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        if ("chatId".equals(ex.getName())) {
            return ResponseEntity.badRequest().body("chatId must be a number");
        }
        return ResponseEntity.badRequest().body("Invalid parameter: " + ex.getName());
    }




    @PostMapping("/id/{chatId}/settings")
    public ResponseEntity<?> upsertSettings(
            @PathVariable @Positive Long chatId,
            @Valid @RequestBody List<@Valid ChatSettingDtoApi> dtoList,
            BindingResult br) {

        if (chatId <= 0) {
            return ResponseEntity.badRequest()
                    .body("chatId must be a positive number"); // 400
        }

        if (br.hasErrors() || dtoList.isEmpty()) {
            String msg = br.getFieldErrors().stream()
                    .map(e -> e.getField() + " " + e.getDefaultMessage())
                    .findFirst()
                    .orElse("Invalid payload");
            return ResponseEntity.badRequest().body(msg);               // 400
        }

        if (!chatRepository.existsById(chatId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)          // 404
                    .body("Chat id " + chatId + " not found");
        }

        dtoList.forEach(d -> {
            ChatSetting s = chatSettingRepository
                    .findByChatIdAndName(chatId, d.name())
                    .orElseGet(ChatSetting::new);

            s.setChatId(chatId);
            s.setName(d.name());
            s.setValue(d.value());
            chatSettingRepository.save(s);
        });

        return ResponseEntity.status(HttpStatus.CREATED)          // 201
                .body(new ApiResponse("Settings saved"));
    }

    /** Simple response wrapper */
    public record ApiResponse(String message) {}
}


//    @PostMapping("/{link}/message")
//    public void sendMessage(@PathVariable("link") String link, @RequestBody MessageDto dto) {
//        Chat chat = chatRepository.findByLink(link).orElseThrow(() -> new RuntimeException("Chat not found"));
//        ChatMessage message = new ChatMessage();
//        message.setMessageText(dto.messageText);
//        message.setSender(dto.sender);
//        message.setRecipient(dto.recipient);
//        message.setTimestamp(dto.timestamp);
//        message.setChat(chat);
//        chatMessageRepository.save(message);
//    }

