package com.practiceproject.linkchat_back.controller;
import com.practiceproject.linkchat_back.dtos.ChatSettingsDtoAPI;
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
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.practiceproject.linkchat_back.model.Chat;
import com.practiceproject.linkchat_back.model.ChatMessage;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Base64;
import java.util.List;
import java.util.Map;
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
        msg.setMessageType(ChatMessage.MessageType.TEXT); // Set as TEXT message
        msg.setChat(chat);
        chatMessageRepository.save(msg);

        return ResponseEntity.ok(new ChatController.ApiResponse("Message sent"));
    }

    @PostMapping("/{link}/image")
    public ResponseEntity<ApiResponse> sendImageMessage(@PathVariable String link,
                                                        @RequestBody Map<String, Object> request) {
        try {
            logger.info("Attempting to send image message for chat link: {}", link);

            Optional<Chat> chatOpt = chatRepository.findByLink(link);
            if (chatOpt.isEmpty()) {
                logger.error("Chat with link {} not found when sending image message", link);
                return ResponseEntity.badRequest()
                        .body(new ApiResponse("Chat not found"));
            }

            String sender = (String) request.get("sender");
            String recipient = (String) request.get("recipient");
            String imageBase64 = (String) request.get("imageBase64");
            String filename = (String) request.get("filename");
            String contentType = (String) request.get("contentType");

            // Validate base64 format
            if (!imageBase64.startsWith("data:image/")) {
                logger.error("Invalid image format - imageBase64 does not start with 'data:image/', actual start: {}",
                        imageBase64.length() > 20 ? imageBase64.substring(0, 20) : imageBase64);
                return ResponseEntity.badRequest()
                        .body(new ApiResponse("Invalid image format"));
            }

            Chat chat = chatOpt.get();
            ChatMessage msg = new ChatMessage();
            msg.setSender(sender);
            msg.setRecipient(recipient);
            msg.setMessageType(ChatMessage.MessageType.IMAGE);
            msg.setTimestamp(java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            msg.setChat(chat);

            // Store the entire base64 string including data URL prefix as text
            msg.setImageData(imageBase64);
            msg.setImageFilename(filename);
            msg.setImageContentType(contentType);
            // Set a default message text for image messages to satisfy NOT NULL constraint
            msg.setMessageText(""); // or you could use "[Image]" or null if you modify the DB

            ChatMessage savedMessage = chatMessageRepository.save(msg);
            logger.info("Successfully saved image message with ID: {} for chat: {}", savedMessage.getMessageId(), chat.getChatId());

            return ResponseEntity.ok(new ApiResponse("Image message sent"));
        } catch (Exception e) {
            logger.error("Error sending image message for chat link: {} - Error: {}", link, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Error sending image message: " + e.getMessage()));
        }
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
        List<ChatSettingsDtoAPI.Setting> settings = chatSettingRepository.findByChatId(chatId)
                .stream()
                .map(s -> new ChatSettingsDtoAPI.Setting(s.getSettingKey(), s.getSettingValue()))
                .toList();

        if (settings.isEmpty()) {
            return ResponseEntity.ok(new ApiResponse("No settings for this chat"));
        }
        return ResponseEntity.ok(new ChatSettingsDtoAPI(settings));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<String> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        if ("chatId".equals(ex.getName())) {
            return ResponseEntity.badRequest().body("chatId must be a number");
        }
        return ResponseEntity.badRequest().body("Invalid parameter: " + ex.getName());
    }

    @PostMapping("/{chatId}/settings")
    public ResponseEntity<?> upsertSettings(
            @PathVariable @Positive Long chatId,
            @Valid @RequestBody ChatSettingsDtoAPI dtoList,
            BindingResult br) {

        if (chatId <= 0) {
            return ResponseEntity.badRequest()
                    .body("chatId must be a positive number"); // 400
        }

        if (br.hasErrors() || dtoList.getSettings() == null || dtoList.getSettings().isEmpty()) {
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

        for (ChatSettingsDtoAPI.Setting sDto : dtoList.getSettings()) {
            ChatSetting setting = chatSettingRepository
                    .findByChatIdAndSettingKey(chatId, sDto.getSettingKey())
                    .orElseGet(ChatSetting::new);

            setting.setChatId(chatId);
            setting.setSettingKey(sDto.getSettingKey());
            setting.setSettingValue(sDto.getSettingValue());

            chatSettingRepository.save(setting);
        }


        return ResponseEntity.status(HttpStatus.CREATED)          // 201
                .body(new ApiResponse("Settings saved"));
    }

    //update
    @PutMapping("/{chatId}/settings")
    public ResponseEntity<?> updateSetting(
            @PathVariable Long chatId,
            @RequestBody Map<String, String> payload) {

        String settingKey = payload.get("settingKey");
        String newValue = payload.get("settingValue");

        if (settingKey == null || settingKey.trim().isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse("settingKey is required"));
        }

        if (newValue == null || newValue.trim().isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse("settingValue is required"));
        }

        Optional<ChatSetting> optionalSetting = chatSettingRepository.findByChatIdAndSettingKey(chatId, settingKey);

        if (optionalSetting.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse("Setting not found"));
        }

        ChatSetting setting = optionalSetting.get();
        setting.setSettingValue(newValue);
        chatSettingRepository.save(setting);

        return ResponseEntity.ok(new ApiResponse("Setting updated"));
    }

    @DeleteMapping("/{chatId}/settings/{settingKey}")
    public ResponseEntity<?> deleteSetting(
            @PathVariable Long chatId,
            @PathVariable String settingKey) {

        Optional<ChatSetting> optionalSetting = chatSettingRepository.findByChatIdAndSettingKey(chatId, settingKey);

        if (optionalSetting.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse("Setting not found"));
        }

        chatSettingRepository.delete(optionalSetting.get());
        return ResponseEntity.ok(new ApiResponse("Setting deleted"));
    }
    @GetMapping("/{link}/image/{messageId}")
    public ResponseEntity<?> getImage(@PathVariable String link, @PathVariable Long messageId) {
        try {
            logger.info("Retrieving image for message ID: {} in chat: {}", messageId, link);

            // Verify chat exists
            Optional<Chat> chatOpt = chatRepository.findByLink(link);
            if (chatOpt.isEmpty()) {
                logger.error("Chat with link {} not found", link);
                return ResponseEntity.notFound().build();
            }

            // Find the message
            Optional<ChatMessage> messageOpt = chatMessageRepository.findById(messageId);
            if (messageOpt.isEmpty()) {
                logger.error("Message with ID {} not found", messageId);
                return ResponseEntity.notFound().build();
            }

            ChatMessage message = messageOpt.get();

            // Verify message belongs to the chat
            if (!message.getChat().getChatId().equals(chatOpt.get().getChatId())) {
                logger.error("Message {} does not belong to chat {}", messageId, link);
                return ResponseEntity.notFound().build();
            }

            // Verify it's an image message
            if (message.getMessageType() != ChatMessage.MessageType.IMAGE || message.getImageData() == null) {
                logger.error("Message {} is not an image or has no image data", messageId);
                return ResponseEntity.notFound().build();
            }

            // Extract base64 data (remove data:image/type;base64, prefix)
            String imageBase64 = message.getImageData();
            String base64Data;
            if (imageBase64.contains(",")) {
                base64Data = imageBase64.substring(imageBase64.indexOf(",") + 1);
            } else {
                base64Data = imageBase64;
            }

            // Decode base64 to bytes
            byte[] imageBytes = Base64.getDecoder().decode(base64Data);

            // Set content type
            String contentType = message.getImageContentType();
            if (contentType == null) {
                contentType = "image/jpeg"; // default fallback
            }

            logger.info("Successfully retrieved image for message ID: {}, size: {} bytes", messageId, imageBytes.length);

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .body(imageBytes);

        } catch (Exception e) {
            logger.error("Error retrieving image for message ID: {} in chat: {} - Error: {}", messageId, link, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Error retrieving image: " + e.getMessage()));
        }
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
