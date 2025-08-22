package com.practiceproject.linkchat_back.dtos;

public class ChatMessageRequest {
    private Long chatId;
    private String sender;
    private String recipient;
    private String messageType;
    private String messageText;

    public ChatMessageRequest () {}

    public ChatMessageRequest(Long chatId, String sender, String recipient, String messageType, String messageText) {
        this.chatId = chatId;
        this.sender = sender;
        this.recipient = recipient;
        this.messageType = messageType;
        this.messageText = messageText;
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }
}
