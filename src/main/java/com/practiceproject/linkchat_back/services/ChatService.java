package com.practiceproject.linkchat_back.services;


import com.practiceproject.linkchat_back.repository.ChatRepository;
import com.practiceproject.linkchat_back.utility.ChatUtils;
import com.practiceproject.linkchat_back.viewModels.ChatForm;
import org.springframework.stereotype.Service;

@Service
public class ChatService {

    private final ChatRepository chatRepository;

    public ChatService(ChatRepository chatRepository) {
        this.chatRepository = chatRepository;
    }

    public void generateRandomLink(ChatForm form) {
        form.setLink(ChatUtils.generateRandomChatLink());
    }
}
