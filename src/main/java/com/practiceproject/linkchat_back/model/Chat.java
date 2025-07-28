package com.practiceproject.linkchat_back.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "chat")
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_id")
    private Long chatId;

    @Column(name = "title", nullable = false)
    private String title;

    private String users; // optional string column as per DB schema

    @Column(name = "type", nullable = false)
    private String type;

    @Column(name = "active", nullable = false)
    private boolean active = true; // Default to active

    @Column(name = "link", nullable = false, unique = true)
    private String link;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "chat_invite_email",
            joinColumns = @JoinColumn(name = "chat_id"))
    @Column(name = "email", nullable = false)
    private List<InviteEmailEntry> inviteEmails;

    public Chat() {}

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUsers() {
        return users;
    }

    public void setUsers(String users) {
        this.users = users;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public List<InviteEmailEntry> getInviteEmails() {
        return inviteEmails;
    }

    public void setInviteEmails(List<InviteEmailEntry> inviteEmails) {
        this.inviteEmails = inviteEmails;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}