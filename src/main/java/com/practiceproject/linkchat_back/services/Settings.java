package com.practiceproject.linkchat_back.services;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.io.Serializable;

@Entity
@Table(
        name = "settings",
        uniqueConstraints = @UniqueConstraint(name = "uk_settings_name", columnNames = "setting_name")
)
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
@ToString
public class Settings implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 64)
    @Column(name = "setting_name", nullable = false, length = 64)
    private String settingName;

    @NotBlank
    @Size(max = 512)
    @Column(name = "setting_value", nullable = false, length = 512)
    private String settingValue;

    @PrePersist @PreUpdate
    private void normalise() {
        settingName  = settingName.trim();
        settingValue = settingValue.trim();
    }
}
