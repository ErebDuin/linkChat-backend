package com.practiceproject.linkchat_back.controller;

import com.practiceproject.linkchat_back.model.Setting1;
import com.practiceproject.linkchat_back.services.Setting1Service;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class Setting1Controller {

    private final Setting1Service service;
    public Setting1Controller(Setting1Service service) {
        this.service = service;
    }

    @GetMapping("/ui/settings1")
    public String settings1(Model model) {
        model.addAttribute("settings", service.findAll());
        model.addAttribute("settingForm", new Setting1());
        return "settings1";
    }

    @PostMapping("/ui/settings1/add")
    public String addSetting(@ModelAttribute Setting1 setting) {
        service.save(setting);
        return "redirect:/ui/settings1";
    }

    @GetMapping("/ui/settings1/delete/{id}")
    public String deleteSetting(@PathVariable Long id) {
        service.deleteById(id);
        return "redirect:/ui/settings1";
    }

    @PostMapping("/ui/settings1/update")
    public String updateSetting(@ModelAttribute Setting1 setting) {
        service.save(setting);
        return "redirect:/ui/settings1";
    }
}
