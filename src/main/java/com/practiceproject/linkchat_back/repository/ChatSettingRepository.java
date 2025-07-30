package com.practiceproject.linkchat_back.repository;

import com.practiceproject.linkchat_back.model.ChatSetting;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ChatSettingRepository extends JpaRepository<ChatSetting, Long> {
    List<ChatSetting> findByChatId(Long chatId);
    Optional<ChatSetting> findByChatIdAndSettingKey(Long chatId, String settingKey);
}