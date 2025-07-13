package com.practiceproject.linkchat_back.viewController;

import com.practiceproject.linkchat_back.model.Setting1;
import com.practiceproject.linkchat_back.services.GegamSettingsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class GegamSettingsController {
    private static final Logger logger = LoggerFactory.getLogger(GegamSettingsController.class);

    private final GegamSettingsService gegamService;
    public GegamSettingsController(GegamSettingsService gegamService) {
        this.gegamService = gegamService;
    }


    @GetMapping("/ui/gegam-settings")
    public String showSettingsPage(@RequestParam(value = "id", required = false) Long id, Model model) {
        if (id != null) {
            Setting1 setting = gegamService.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid setting ID: " + id));
            model.addAttribute("setting", setting);
            model.addAttribute("editing", true);
        } else {
            model.addAttribute("setting", new Setting1());
            model.addAttribute("editing", false);
        }
        return "gegam-settings";
    }

    @PostMapping("/ui/gegam-settings")
    public String saveSetting(@ModelAttribute Setting1 setting, Model model) {
        if (setting.getId() != null && gegamService.findById(setting.getId()).isPresent()) {
            logger.info("Updating existing setting with ID: {}", setting.getId());
            model.addAttribute("error", "Setting with that name already exists.");
        } else {
            logger.info("Saving new setting: {}", setting.getSettingName());
            model.addAttribute("message", "Setting saved successfully!");
        }
        gegamService.saveSetting(setting);
        model.addAttribute("setting", new Setting1());
        model.addAttribute("editing", false);
        return "gegam-settings";
    }

    @GetMapping("/ui/view-settings")
    public String viewSettings(Model model) {
        logger.info("Fetching all settings for view");
        model.addAttribute("settingsList", gegamService.findAllSettings());
        return "view-settings";
    }

    @PostMapping("/ui/update-setting")
    public String updateSetting(@ModelAttribute Setting1 setting) {
        logger.info("Updating setting with ID: {}", setting.getId());
        gegamService.saveSetting(setting);
        return "redirect:/ui/view-settings";
    }

    @GetMapping("/ui/delete-setting/{id}")
    public String deleteSetting(@PathVariable Long id) {
        logger.info("Deleting setting with ID: {}", id);
        gegamService.deleteById(id);
        return "redirect:/ui/view-settings";
    }
}
