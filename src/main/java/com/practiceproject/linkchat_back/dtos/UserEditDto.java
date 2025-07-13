package com.practiceproject.linkchat_back.dtos;

import jakarta.validation.constraints.*;


public class UserEditDto {

    @NotNull private Long id;
    @NotBlank @Size(min = 2, max = 50) private String name;
    @Email @NotBlank private String email;
    @NotBlank private String role;
    private boolean active;

    public UserEditDto() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}