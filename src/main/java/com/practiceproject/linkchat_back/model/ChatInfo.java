package com.practiceproject.linkchat_back.model;
import com.practiceproject.linkchat_back.repository.ChatMessageRepository;
import com.practiceproject.linkchat_back.repository.ChatRepository;
import com.practiceproject.linkchat_back.repository.ChatUserRepository;
import com.practiceproject.linkchat_back.model.Chat;

import java.util.ArrayList;
import java.util.List;

public class ChatInfo {
    private String title;
    private String link;
    private List<User> users;
    private List<ChatMessage> messages;

    public ChatInfo(String title, String link) {
        this.title = title;
        this.link = link;
        this.users = new ArrayList<>();
        this.messages = new ArrayList<>();
    }

    public ChatInfo(String link,
                    ChatRepository chatRepository,
                    ChatUserRepository chatUserRepository,
                    ChatMessageRepository messageRepository) {
            try {
                Chat chat = chatRepository.findByLink(link).orElse(null);
                if (chat != null) {
                    long chatId = chat.getChatId();
                    this.title = chat.getTitle();
                    this.link = chat.getLink();
                    this.users = chatUserRepository.getChatUsers(chatId);

                    // Try to get messages with error handling
                    try {
                        this.messages = messageRepository.getMessagesByChatId(chatId);
                    } catch (Exception e) {
                        // Fallback to empty list if there's an issue
                        System.err.println("Error fetching messages for chat " + chatId + ": " + e.getMessage());
                        this.messages = new ArrayList<>();
                    }
                } else {
                    this.title = null;
                    this.link = null;
                    this.users = new ArrayList<>();
                    this.messages = new ArrayList<>();
                }
            } catch (Exception e) {
                System.err.println("Error in ChatInfo constructor: " + e.getMessage());
                this.title = null;
                this.link = null;
                this.users = new ArrayList<>();
                this.messages = new ArrayList<>();
            }
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public List<ChatMessage> getMessages() {
        return messages;
    }

    public void setMessages(List<ChatMessage> messages) {
        this.messages = messages;
    }
}
