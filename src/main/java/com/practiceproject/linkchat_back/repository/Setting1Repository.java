package com.practiceproject.linkchat_back.repository;

import com.practiceproject.linkchat_back.model.Setting1;
import org.springframework.data.jpa.repository.JpaRepository;

public interface Setting1Repository extends JpaRepository<Setting1, Long> {
    boolean existsBySettingName(String settingName);
    Setting1 findBySettingName(String settingName);
}
