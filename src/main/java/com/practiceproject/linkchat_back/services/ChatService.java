package com.practiceproject.linkchat_back.services;


import com.practiceproject.linkchat_back.dtos.ChatSettingsDto;
import com.practiceproject.linkchat_back.model.Chat;
import com.practiceproject.linkchat_back.model.InviteEmailEntry;
import com.practiceproject.linkchat_back.repository.ChatRepository;
import com.practiceproject.linkchat_back.utility.ChatUtils;
import com.practiceproject.linkchat_back.viewModels.ChatForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

@Service
public class ChatService {

    private final ChatRepository chatRepository;

    public ChatService(ChatRepository chatRepository) {
        this.chatRepository = chatRepository;
    }

    public ChatForm createDefaultChatForm() {
        ChatForm form = new ChatForm();
        form.setType(null);
        if (form.getInviteEmails().isEmpty()) {
            form.getInviteEmails().add(new InviteEmailEntry());
        }
        return form;
    }

    public void generateRandomLink(ChatForm form) {
        form.setLink(ChatUtils.generateRandomChatLink());
        form.getInviteEmails().removeIf(entry -> entry.getEmail() == null || entry.getEmail().trim().isEmpty());
        if (form.getInviteEmails().isEmpty()) {
            form.getInviteEmails().add(new InviteEmailEntry());
        }
    }

    public void addInviteEmail(ChatForm form, String email) {
        for (InviteEmailEntry entry : form.getInviteEmails()) {
            if (entry.getEmail() != null && !entry.getEmail().isBlank()) {
                entry.setInvitedAt(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
            }
        }
        if (email != null && !email.isBlank()) {
            InviteEmailEntry newEntry = new InviteEmailEntry();
            newEntry.setEmail(email);
            newEntry.setInvitedAt(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
            form.getInviteEmails().add(newEntry);
        }
    }

    public void saveChat(ChatForm form) {
        Chat chat = new Chat();
        chat.setTitle(form.getTitle());
        chat.setLink(form.getLink());
        chat.setType(form.getType());
        chat.setActive(form.isActive());
        chat.setInviteEmails(new ArrayList<>(form.getInviteEmails()));
        chatRepository.save(chat);
    }

    public void updateChatSettings(ChatSettingsDto dto) {
        Chat chat = chatRepository.findById(dto.getId())
                .orElseThrow(() -> new IllegalArgumentException("Chat not found with ID: " + dto.getId()));

        chat.setTitle(dto.getTitle());
        chat.setLink(dto.getLink());
        chat.setType(dto.getType());
        chat.setActive(dto.isActive());

        chatRepository.save(chat);
    }

    public void deleteChatById(Long id) {
        if (!chatRepository.existsById(id)) {
            throw new IllegalArgumentException("Chat not found with ID: " + id);
        }
        chatRepository.deleteById(id);
    }
}
