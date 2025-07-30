package com.practiceproject.linkchat_back.model;

import jakarta.persistence.*;

@Entity
@Table(name = "chat_settings",
        uniqueConstraints = @UniqueConstraint(columnNames = {"chat_id", "setting_key"}))
public class ChatSetting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "chat_id", nullable = false)
    private Long chatId;

    @Column(name = "setting_key", nullable = false)
    private String settingKey;

    @Column(name = "setting_value", nullable = false)
    private String settingValue;

    public ChatSetting() {}

    public ChatSetting(Long chatId, String settingKey, String settingValue) {
        this.chatId = chatId;
        this.settingKey = settingKey;
        this.settingValue = settingValue;
    }

    // Getters и Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public String getSettingKey() {
        return settingKey;
    }

    public void setSettingKey(String settingKey) {
        this.settingKey = settingKey;
    }

    public String getSettingValue() {
        return settingValue;
    }

    public void setSettingValue(String settingValue) {
        this.settingValue = settingValue;
    }
}
