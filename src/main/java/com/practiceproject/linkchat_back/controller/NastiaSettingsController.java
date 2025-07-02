package com.practiceproject.linkchat_back.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import com.practiceproject.linkchat_back.model.NastiaSettings;
import com.practiceproject.linkchat_back.services.NastiaSettingsService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

public class NastiaSettingsController {

    private final NastiaSettingsService settingsService;

    @Autowired
    public NastiaSettingsController(NastiaSettingsService settingsService) {
        this.settingsService = settingsService;
    }

    @GetMapping("/settings")
    public List<NastiaSettings> getAllSettings() {
        return settingsService.getAllSettings();
    }

    @GetMapping("/settings/{id}")
    public NastiaSettings getSettingById(@PathVariable Long id) {
        return settingsService.getSettingById(id);
    }

    @PostMapping("/settings")
    public NastiaSettings createSetting(@RequestBody NastiaSettings setting) {
        return settingsService.createSetting(setting);
    }

    @PutMapping("/settings/{id}")
    public NastiaSettings updateSetting(@PathVariable Long id, @RequestBody NastiaSettings setting) {
        return settingsService.updateSetting(id, setting);
    }

    @DeleteMapping("/settings/{id}")
    public void deleteSetting(@PathVariable Long id) {
        settingsService.deleteSetting(id);
    }
}
