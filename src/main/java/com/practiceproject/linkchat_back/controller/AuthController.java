package com.practiceproject.linkchat_back.controller;


import com.practiceproject.linkchat_back.dtos.UserDto;
import com.practiceproject.linkchat_back.model.RequestResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


import java.util.Objects;

@RestController
public class AuthController {
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @PostMapping("/api/auth")
    public ResponseEntity<RequestResponse> auth(@RequestBody final UserDto user) {
        logger.debug("Accessing auth endpoint");

        if (user == null ||
                user.getUsername() == null ||
                user.getPassword() == null) {

            logger.error("Authentication failed: missing credentials");
            return ResponseEntity.badRequest().body(new RequestResponse("Authentication failed: missing credentials"));
        }

        boolean isAuthenticated =
                Objects.equals("testuser", user.getUsername()) &&
                        Objects.equals("password", user.getPassword());

        if (!isAuthenticated) {
            logger.error("Authentication failed: invalid username or password");
            return ResponseEntity.status(401).body(new RequestResponse("Authentication failed: invalid username or password"));
        }

        logger.info("Authentication successful for user: {}", user.getUsername());
        return ResponseEntity.ok(new RequestResponse("Authentication successful"));
    }
}
