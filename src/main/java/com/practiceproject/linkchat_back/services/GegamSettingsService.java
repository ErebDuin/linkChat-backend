package com.practiceproject.linkchat_back.services;

import com.practiceproject.linkchat_back.model.Setting1;
import com.practiceproject.linkchat_back.repository.Setting1Repository;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;


@Service
public class GegamSettingsService {

    private final Setting1Repository setting1Repository;

    public GegamSettingsService(Setting1Repository setting1Repository) {
        this.setting1Repository = setting1Repository;
    }

    // Method to show the settings page




    // Method to save a setting
    public void saveSetting(Setting1 setting) {
        setting1Repository.save(setting);
    }

    // Method to find all settings
    public Iterable<Setting1> findAllSettings() {
        return setting1Repository.findAll();
    }

    // Method to find a setting by its ID
    public Optional<Setting1> findById(Long id) {
        return setting1Repository.findById(id);
    }

    // Method to delete a setting by its ID
    public void deleteById(Long id) {
        setting1Repository.deleteById(id);
    }
}
