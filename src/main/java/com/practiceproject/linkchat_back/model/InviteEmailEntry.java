package com.practiceproject.linkchat_back.model;


import jakarta.persistence.*;

import java.time.LocalDateTime;

@Embeddable
public class InviteEmailEntry {
    @Column(name = "email")
    private String email;

    @Column(name = "invited_at", nullable = false)
    private LocalDateTime invitedAt;

    public InviteEmailEntry() {}

    public InviteEmailEntry(String email, LocalDateTime invitedAt) {
        this.email = email;
        this.invitedAt = invitedAt;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDateTime getInvitedAt() {
        return invitedAt;
    }

    public void setInvitedAt(LocalDateTime invitedAt) {
        this.invitedAt = invitedAt;
    }
}
