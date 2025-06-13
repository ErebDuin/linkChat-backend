package com.practiceproject.linkchat_back.repository;

import com.practiceproject.linkchat_back.model.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ChatRepository extends JpaRepository<Chat, Long> {
    Optional<Chat> findByLink(String link);
}