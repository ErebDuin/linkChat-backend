package com.practiceproject.linkchat_back.services;

import com.practiceproject.linkchat_back.model.Setting1;
import com.practiceproject.linkchat_back.repository.Setting1Repository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ElenaSettingsService {

    private final Setting1Repository setting1Repository;

    public ElenaSettingsService(Setting1Repository setting1Repository) {

        this.setting1Repository = setting1Repository;
    }

    public void saveSetting(Setting1 setting) {

        setting1Repository.save(setting);
    }


    public Iterable<Setting1> findAllSettings() {

        return setting1Repository.findAll();
    }


    public Optional<Setting1> findById(Long id) {

    return setting1Repository.findById(id);
    }


    public void deleteById(Long id) {

        setting1Repository.deleteById(id);
    }
}
