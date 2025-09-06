package com.practiceproject.linkchat_back.viewController;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/ui")
public class AdminController {
    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

    @GetMapping("/admin-login")
    public String login() {
        logger.debug("Accessing admin login page");
        return "admin-login";
    }

    @GetMapping("/admin-register")
    public String register() {
        logger.debug("Accessing admin registration page");
        return "admin-register";
    }
}
