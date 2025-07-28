package com.practiceproject.linkchat_back.model;

import jakarta.persistence.*;

@Entity
@Table(name = "chat_settings",
        uniqueConstraints = @UniqueConstraint(columnNames = {"chat_id","name"}))
public class ChatSetting {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "chat_id", nullable = false)
    private Long chatId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String value;

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}
