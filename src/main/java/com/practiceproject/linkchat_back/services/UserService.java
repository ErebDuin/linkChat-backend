package com.practiceproject.linkchat_back.services;

import com.practiceproject.linkchat_back.dtos.AdminRegistrationDto;
import com.practiceproject.linkchat_back.dtos.UserRegistrationDto;
import com.practiceproject.linkchat_back.enums.UserRole;
import com.practiceproject.linkchat_back.exceptions.EmailAlreadyExistsException;
import com.practiceproject.linkchat_back.exceptions.PasswordMismatchException;
import com.practiceproject.linkchat_back.model.User;
import com.practiceproject.linkchat_back.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    public void saveAdminUser(AdminRegistrationDto registrationDto) {
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


    public void saveNewUser(UserRegistrationDto registrationDto) {
        if (UserRole.ADMIN.equals(registrationDto.getRole())) {
            if (registrationDto.getPassword() == null || registrationDto.getConfirmPassword() == null) {
                throw new IllegalArgumentException("Password required for admin accounts.");
            }
            if (!registrationDto.getPassword().equals(registrationDto.getConfirmPassword())) {
                throw new PasswordMismatchException();
            }
            if (userRepository.existsByEmail(registrationDto.getEmail())) {
                throw new EmailAlreadyExistsException(registrationDto.getEmail());
            }

            User newUser = new User();
            newUser.setActive(true);
            newUser.setUsername(registrationDto.getUsername());
            newUser.setEmail(registrationDto.getEmail());
            newUser.setPassword(passwordEncoder.encode(registrationDto.getPassword()));
            newUser.setRole(registrationDto.getRole());

            User savedNewAdminUser = userRepository.save(newUser);
            logger.info("Saved new admin user: {}", savedNewAdminUser);
        } else {
            if (userRepository.existsByEmail(registrationDto.getEmail())) {
                throw new EmailAlreadyExistsException(registrationDto.getEmail());
            }

            User newUser = new User();
            newUser.setActive(true);
            newUser.setUsername(registrationDto.getUsername());
            newUser.setEmail(registrationDto.getEmail());
            String randomPassword = UUID.randomUUID().toString();
            newUser.setPassword(passwordEncoder.encode(randomPassword));
            newUser.setRole(registrationDto.getRole());

            User savedNewUser = userRepository.save(newUser);
            logger.info("Saved new user: {}", savedNewUser);
        }
    }
}
