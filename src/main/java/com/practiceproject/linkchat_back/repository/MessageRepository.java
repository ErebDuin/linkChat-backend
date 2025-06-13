package com.practiceproject.linkchat_back.repository;

import com.practiceproject.linkchat_back.model.Message;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByChatId(long chatId);
    List<Message> findByFromUser(String fromUser);
    List<Message> findByToUser(String toUser);
    List<Message> findByChatIdAndMessageTextContaining(long chatId, String messageText);
}