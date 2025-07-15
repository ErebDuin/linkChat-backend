package com.practiceproject.linkchat_back.services;


import com.practiceproject.linkchat_back.model.Chat;
import com.practiceproject.linkchat_back.repository.ChatRepository;
import com.practiceproject.linkchat_back.utility.ChatUtils;
import com.practiceproject.linkchat_back.viewModels.ChatForm;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class ChatService {

    private final ChatRepository chatRepository;

    public ChatService(ChatRepository chatRepository) {
        this.chatRepository = chatRepository;
    }

    public void generateRandomLink(ChatForm form) {
        form.setLink(ChatUtils.generateRandomChatLink());
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
}
