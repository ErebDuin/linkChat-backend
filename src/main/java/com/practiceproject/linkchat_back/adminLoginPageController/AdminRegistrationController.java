package com.practiceproject.linkchat_back.adminLoginPageController;

import com.practiceproject.linkchat_back.model.User; // Import the User class
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AdminRegistrationController {

    private final UserService userService;

    @Autowired
    public AdminRegistrationController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/createDefaultAdmin")
    public ResponseEntity<String> createDefaultAdmin() {
        // Create a default admin user
        User defaultAdmin = new User();
        defaultAdmin.setUsername("defaultAdmin");
        defaultAdmin.setPassword("defaultPassword");

        userService.registerAdmin(defaultAdmin);
        return ResponseEntity.ok("Default admin created successfully");
    }
}