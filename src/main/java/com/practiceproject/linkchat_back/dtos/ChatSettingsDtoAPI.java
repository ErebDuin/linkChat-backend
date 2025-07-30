package com.practiceproject.linkchat_back.dtos;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import java.util.List;

public class ChatSettingsDtoAPI {

    public static class Setting {
        @NotBlank
        String settingKey;
        @NotBlank
        private String settingValue;

        public Setting() {};

        public Setting(String settingKey, String settingValue) {
            this.settingKey = settingKey;
            this.settingValue = settingValue;
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

    @Valid
    private List<Setting> settings;

    public ChatSettingsDtoAPI() {}

    public ChatSettingsDtoAPI(List<Setting> settings) {
        this.settings = settings;
    }

    public List<Setting> getSettings() {
        return settings;
    }

    public void setSettings(List<Setting> settings) {
        this.settings = settings;
    }
}
