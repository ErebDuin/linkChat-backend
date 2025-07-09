package com.practiceproject.linkchat_back.viewController;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class chatsManagementController {

    @GetMapping("/ui/chats-management")
    public String showChatsManagement() {
        return "chats-management";
    }
}
