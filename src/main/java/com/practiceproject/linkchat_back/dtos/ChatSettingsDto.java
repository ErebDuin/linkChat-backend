package com.practiceproject.linkchat_back.dtos;

// src/main/java/com/practiceproject/linkchat_back/viewModels/ChatEditDto.java


public class ChatSettingsDto {
    private Long id;
    private String title;
    private String link;
    private String type;
    private boolean active;

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getLink() { return link; }
    public void setLink(String link) { this.link = link; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}
