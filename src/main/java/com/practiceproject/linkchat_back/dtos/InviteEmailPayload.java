package com.practiceproject.linkchat_back.dtos;

import java.io.Serializable;
import java.util.List;

public class InviteEmailPayload implements Serializable {
    private List<String> recipients;
    private String chatSubject;
    private String chatLink;
    private String chatTitle;

    public List<String> getRecipients() {
        return recipients;
    }

    public void setRecipients(List<String> recipients) {
        this.recipients = recipients;
    }

    public String getChatSubject() {
        return chatSubject;
    }

    public void setChatSubject(String chatSubject) {
        this.chatSubject = chatSubject;
    }

    public String getChatTitle() {
        return chatTitle;
    }

    public void setChatTitle(String chatTitle) {
        this.chatTitle = chatTitle;
    }

    public String getChatLink() {
        return chatLink;
    }

    public void setChatLink(String chatLink) {
        this.chatLink = chatLink;
    }

    public InviteEmailPayload() {}

    public InviteEmailPayload(List<String> recipients, String chatSubject ,String chatTitle, String chatLink) {
        this.recipients = recipients;
        this.chatSubject = chatSubject;
        this.chatTitle = chatTitle;
        this.chatLink = chatLink;
    }
}
