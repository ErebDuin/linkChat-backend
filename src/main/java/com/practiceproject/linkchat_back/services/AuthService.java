package com.practiceproject.linkchat_back.services;

import com.practiceproject.linkchat_back.model.User;
import com.practiceproject.linkchat_back.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public boolean authenticate(String username, String rawPassword) {
        User user = userRepository.findByUsername(username).orElse(null);
        if (user == null) {
            return false;
        }

        return passwordEncoder.matches(rawPassword, user.getPassword());
    }
}