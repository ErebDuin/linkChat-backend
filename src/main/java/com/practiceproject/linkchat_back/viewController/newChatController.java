package com.practiceproject.linkchat_back.viewController;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class newChatController {

    @GetMapping("/ui/new-chat")
    public String showNewChat() {
        return "new-chat";
    }
}
