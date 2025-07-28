package com.practiceproject.linkchat_back.dtos;

import com.practiceproject.linkchat_back.model.ChatMessage;

public class ChatMessageResponse {
    private Long messageId;
    private String messageText;
    private String messageType;
    private String imageBase64;
    private String imageFilename;
    private String imageContentType;
    private String timestamp;
    private String sender;
    private String recipient;
    private Long chatId;

    public ChatMessageResponse() {}

    public ChatMessageResponse(ChatMessage message) {
        this.messageId = message.getMessageId();
        this.messageText = message.getMessageText();
        this.messageType = message.getMessageType().name();
        this.timestamp = message.getTimestamp();
        this.sender = message.getSender();
        this.recipient = message.getRecipient();
        this.chatId = message.getChat() != null ? message.getChat().getChatId() : null;

        if (message.getMessageType() == ChatMessage.MessageType.IMAGE && message.getImageData() != null) {
            this.imageBase64 = java.util.Base64.getEncoder().encodeToString(message.getImageData());
            this.imageFilename = message.getImageFilename();
            this.imageContentType = message.getImageContentType();
        }
    }

    // Getters and setters
    public Long getMessageId() { return messageId; }
    public void setMessageId(Long messageId) { this.messageId = messageId; }

    public String getMessageText() { return messageText; }
    public void setMessageText(String messageText) { this.messageText = messageText; }

    public String getMessageType() { return messageType; }
    public void setMessageType(String messageType) { this.messageType = messageType; }

    public String getImageBase64() { return imageBase64; }
    public void setImageBase64(String imageBase64) { this.imageBase64 = imageBase64; }

    public String getImageFilename() { return imageFilename; }
    public void setImageFilename(String imageFilename) { this.imageFilename = imageFilename; }

    public String getImageContentType() { return imageContentType; }
    public void setImageContentType(String imageContentType) { this.imageContentType = imageContentType; }

    public String getTimestamp() { return timestamp; }
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }

    public String getSender() { return sender; }
    public void setSender(String sender) { this.sender = sender; }

    public String getRecipient() { return recipient; }
    public void setRecipient(String recipient) { this.recipient = recipient; }

    public Long getChatId() { return chatId; }
    public void setChatId(Long chatId) { this.chatId = chatId; }
}
