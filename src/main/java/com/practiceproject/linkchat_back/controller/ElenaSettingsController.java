package com.practiceproject.linkchat_back.controller;


import com.practiceproject.linkchat_back.model.Setting1;
import com.practiceproject.linkchat_back.model.VitaliiSettings;
import com.practiceproject.linkchat_back.services.ElenaSettingsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ElenaSettingsController {

    private final ElenaSettingsService service;

    public ElenaSettingsController(ElenaSettingsService service) {
        this.service = service;
    }

    @GetMapping("/ui/elena-settings")
    public String listSettings(Model model) {
        model.addAttribute("settings", service.findAllSettings());
        model.addAttribute("settingForm", new Setting1());
        return "elena-settings";
    }

    @PostMapping ("/ui/elena-settings/add")
    public String addSetting(@ModelAttribute Setting1 setting) {
        service.saveSetting(setting);
        return "redirect:/ui/elena-settings";
    }
    @GetMapping("/ui/elena-settings/delete/{id}")
    public String deleteSetting(@PathVariable Long id) {
        service.deleteById(id);
        return "redirect:/ui/elena-settings";
    }

    @PostMapping("/ui/elena-settings/update")
    public String updateSetting(@ModelAttribute Setting1 setting) {
        service.saveSetting(setting);
        return "redirect:/ui/vlad-settings";
    }
}
