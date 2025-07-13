package com.practiceproject.linkchat_back.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class MessageDto {

    @NotBlank  public String messageText;
    @NotBlank  public String sender;
    @NotBlank  public String recipient;
    @NotNull public String timestamp;

    public record MessageResponse(String message) {}
}
