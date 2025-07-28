package com.practiceproject.linkchat_back.viewModels;

import com.practiceproject.linkchat_back.model.InviteEmailEntry;
import com.practiceproject.linkchat_back.validators.UniqueTitle;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.UniqueElements;

import java.util.ArrayList;
import java.util.List;

public class ChatForm {

    @NotBlank(message="Title is required")
    @Size(min = 2, max = 20, message = "Name must be between 2 and 20 characters")
    @UniqueTitle
    private String title;

    @NotNull
    private String link;

    @NotBlank(message = "Type is required")
    private String type;

    private boolean active = true;

    @UniqueElements
    private List<InviteEmailEntry> inviteEmails = new ArrayList<>();


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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public List<InviteEmailEntry> getInviteEmails() {
        return inviteEmails;
    }

    public void setInviteEmails(List<InviteEmailEntry> inviteEmails) {
        this.inviteEmails = inviteEmails;
    }
}
