package com.practiceproject.linkchat_back.adminLoginPageController;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminLoginPageController {
    @GetMapping("/api/admin-login")
    public String login() {
        return "/admin-login-page/admin-login";
    }
}
