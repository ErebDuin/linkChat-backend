package com.practiceproject.linkchat_back.services;

import com.practiceproject.linkchat_back.controller.AuthController;
import com.practiceproject.linkchat_back.dtos.UserRegistrationDto;
import com.practiceproject.linkchat_back.enums.UserRole;
import com.practiceproject.linkchat_back.exceptions.EmailAlreadyExistsException;
import com.practiceproject.linkchat_back.exceptions.PasswordMismatchException;
import com.practiceproject.linkchat_back.model.User;
import com.practiceproject.linkchat_back.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    public void saveAdminUser(UserRegistrationDto registrationDto) {
        if (!registrationDto.getPassword().equals(registrationDto.getConfirmPassword())) {
            throw new PasswordMismatchException();
        }

        if (userRepository.existsByEmail(registrationDto.getEmail())) {
            throw new EmailAlreadyExistsException(registrationDto.getEmail());
        }

        User adminUser = new User();
        adminUser.setActive(true);
        adminUser.setUsername(registrationDto.getUsername());
        adminUser.setEmail(registrationDto.getEmail());
        adminUser.setPassword(passwordEncoder.encode(registrationDto.getPassword()));
        adminUser.setRole(UserRole.ADMIN);

        User savedAdmin = userRepository.save(adminUser);
        logger.info("Saved admin user: {}", savedAdmin);
    }


    public void saveChatUser(UserRegistrationDto registrationDto) {
        if (!registrationDto.getPassword().equals(registrationDto.getConfirmPassword())) {
            throw new PasswordMismatchException();
        }

        if (userRepository.existsByEmail(registrationDto.getEmail())) {
            throw new EmailAlreadyExistsException(registrationDto.getEmail());
        }

        User chatUser = new User();
        chatUser.setActive(true);
        chatUser.setUsername(registrationDto.getUsername());
        chatUser.setEmail(registrationDto.getEmail());
        chatUser.setPassword(passwordEncoder.encode(registrationDto.getPassword()));
        chatUser.setRole(UserRole.USER);

        User savedChatUser = userRepository.save(chatUser);
        logger.info("Saved chat user: {}", savedChatUser);
    }
}
