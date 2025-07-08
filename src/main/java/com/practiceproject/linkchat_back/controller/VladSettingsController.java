package com.practiceproject.linkchat_back.controller;
import com.practiceproject.linkchat_back.model.VitaliiSettings;
import com.practiceproject.linkchat_back.services.VitaliiSettingsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class VladSettingsController {

    private final VitaliiSettingsService service;

    public VladSettingsController(VitaliiSettingsService service) {
        this.service = service;
    }

    @GetMapping("/ui/vlad-settings")
    public String listSettings(Model model) {
        model.addAttribute("settings", service.findAll());
        model.addAttribute("settingForm", new VitaliiSettings());
        return "vlad-settings";
    }

    @PostMapping("/ui/vlad-settings/add")
    public String addSetting(@ModelAttribute VitaliiSettings setting) {
        service.save(setting);
        return "redirect:/ui/vlad-settings";
    }

    @GetMapping("/ui/vlad-settings/delete/{id}")
    public String deleteSetting(@PathVariable Long id) {
        service.deleteById(id);
        return "redirect:/ui/vlad-settings";
    }

    @PostMapping("/ui/vlad-settings/update")
    public String updateSetting(@ModelAttribute VitaliiSettings setting) {
        service.save(setting);
        return "redirect:/ui/vlad-settings";
    }

}
