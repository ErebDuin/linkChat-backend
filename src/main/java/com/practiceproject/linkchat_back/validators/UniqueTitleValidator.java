package com.practiceproject.linkchat_back.validators;

import com.practiceproject.linkchat_back.repository.ChatRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UniqueTitleValidator implements ConstraintValidator<UniqueTitle, String> {

    @Autowired
    private ChatRepository chatRepository;

    @Override
    public boolean isValid(String title, ConstraintValidatorContext context) {
        if (title == null || title.trim().isEmpty()) {
            return true;
        }

        return !chatRepository.existsByTitle(title.trim());
    }
}
