package com.practiceproject.linkchat_back.viewModels;

import jakarta.validation.constraints.*;

public class ChatMessage1ViewModel {

    private Long message_id;

    @NotBlank(message = "Recipient is required")
    @Size(min = 2, max = 50, message = "Recipient must be between 2 and 50 characters")
    private String recipient;

    @NotBlank(message = "Sender is required")
    @Size(min = 2, max = 50, message = "Sender must be between 2 and 50 characters")
    private String sender;

    @NotBlank(message = "Timestamp is required")
    private String timestamp;

    public Long getMessage_id() {
        return message_id;
    }

    public void setMessage_id(Long message_id) {
        this.message_id = message_id;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
