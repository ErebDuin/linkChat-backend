package com.practiceproject.linkchat_back.services;

import com.practiceproject.linkchat_back.model.Setting1;
import com.practiceproject.linkchat_back.repository.Setting1Repository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class Setting1Service {

    private final Setting1Repository repository;

    public Setting1Service(Setting1Repository repository) {
        this.repository = repository;
    }

    public List<Setting1> findAll() {
        return repository.findAll();
    }

    public void save(Setting1 setting) {
        repository.save(setting);
    }

    public Optional<Setting1> findById(Long id) {
        return repository.findById(id);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
