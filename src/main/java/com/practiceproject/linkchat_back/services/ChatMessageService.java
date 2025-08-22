package com.practiceproject.linkchat_back.services;

import com.practiceproject.linkchat_back.dtos.ChatMessageRequest;
import com.practiceproject.linkchat_back.dtos.ChatMessageResponse;
import com.practiceproject.linkchat_back.dtos.ImageMessageRequest;
import com.practiceproject.linkchat_back.model.Chat;
import com.practiceproject.linkchat_back.model.ChatMessage;
import com.practiceproject.linkchat_back.producer.ChatMessageProducer;
import com.practiceproject.linkchat_back.repository.ChatMessageRepository;
import com.practiceproject.linkchat_back.repository.ChatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChatMessageService {

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private ChatMessageProducer messageProducer;

    public ChatMessage sendTextMessage(ChatMessageRequest request) {
        ChatMessage message = new ChatMessage();
        Chat chat = chatRepository.findById(request.getChatId()).orElse(null);
        message.setChat(chat);
        message.setSender(request.getSender());
        message.setRecipient(request.getRecipient());
        message.setMessageText(request.getMessageText());
        message.setMessageType(ChatMessage.MessageType.TEXT);
        message.setTimestamp(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));

        ChatMessage saved = chatMessageRepository.save(message);

        try {
            messageProducer.start(saved);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return saved;
    }

    public ChatMessage sendImageMessage(ImageMessageRequest request) {
        ChatMessage message = new ChatMessage();
        message.setSender(request.getSender());
        message.setRecipient(request.getRecipient());
        message.setMessageType(ChatMessage.MessageType.IMAGE);
        message.setTimestamp(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));

        // Store the entire base64 string including data URL prefix as text
        message.setImageData(request.getImageBase64());
        message.setImageFilename(request.getFilename());
        message.setImageContentType(request.getContentType());

        if (request.getChatId() != null) {
            Chat chat = chatRepository.findById(request.getChatId()).orElse(null);
            message.setChat(chat);
        }

        return chatMessageRepository.save(message);
    }

    public List<ChatMessageResponse> getMessagesByChatId(Long chatId) {
        List<ChatMessage> messages = chatMessageRepository.getMessagesByChatId(chatId);
        return messages.stream()
                .map(ChatMessageResponse::new)
                .collect(Collectors.toList());
    }

    public List<ChatMessageResponse> getAllMessages() {
        List<ChatMessage> messages = chatMessageRepository.findAllByOrderByTimestampAsc();
        return messages.stream()
                .map(ChatMessageResponse::new)
                .collect(Collectors.toList());
    }

    public ChatMessage findById(Long id) {
        return chatMessageRepository.findById(id).orElse(null);
    }

    public void deleteById(Long id) {
        chatMessageRepository.deleteById(id);
    }

    public long countMessages() {
        return chatMessageRepository.count();
    }
}
