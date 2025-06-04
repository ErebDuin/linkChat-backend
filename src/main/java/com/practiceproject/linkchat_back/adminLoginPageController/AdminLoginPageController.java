package com.practiceproject.linkchat_back.adminLoginPageController;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequestMapping("/admin/login")
public class AdminLoginPageController {
    private static final Logger logger = LoggerFactory.getLogger(AdminLoginPageController.class);

    @GetMapping("/admin/admin-login")
    public String login() {
        logger.debug("Accessing admin login page");
        return "admin-login";
    }
}
