package com.practiceproject.linkchat_back.repository;

import com.practiceproject.linkchat_back.model.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    @Query("select m from ChatMessage m where m.chat.chatId = :chatId")
    List<ChatMessage> getMessagesByChatId(@Param("chatId") long chatId);
}