package com.practiceproject.linkchat_back.viewController;

import com.practiceproject.linkchat_back.model.Chat;
import com.practiceproject.linkchat_back.repository.ChatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class chatsManagementController {

    @Autowired
    private ChatRepository chatRepository;

    @GetMapping("/ui/chats-management")
    public String showChatsManagement(Model model) {
        List<Chat> chats = chatRepository.findAll();
        model.addAttribute("chats", chats);
        return "chats-management";
    }
}