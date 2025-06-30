package com.practiceproject.linkchat_back.model;

import jakarta.persistence.*;

@Entity
@Table(name = "settings")
public class Setting1 {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "settingName", nullable = false)
    private String settingName;

    @Column(name = "settingType", nullable = false)
    private String settingType;

    @Column(name = "settingValue", nullable = false)
    private String settingValue;

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSettingName() {
        return settingName;
    }

    public void setSettingName(String settingName) {
        this.settingName = settingName;
    }

    public String getSettingType() {
        return settingType;
    }

    public void setSettingType(String settingType) {
        this.settingType = settingType;
    }

    public String getSettingValue() {
        return settingValue;
    }

    public void setSettingValue(String settingValue) {
        this.settingValue = settingValue;
    }
}
