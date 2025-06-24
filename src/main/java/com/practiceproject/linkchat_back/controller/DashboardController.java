package com.practiceproject.linkchat_back.controller;

import com.practiceproject.linkchat_back.model.Chat;
import com.practiceproject.linkchat_back.repository.ChatRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class DashboardController {

    private String message = "";

    private final ChatRepository chatRepository;

    public DashboardController(ChatRepository chatRepository) {
        this.chatRepository = chatRepository;
    }

    @GetMapping("/ui/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("message", message);
        return "dashboard";
    }

    @PostMapping("/ui/dashboard")
    public String dashboard(@RequestParam String newMessage) {
        this.message = newMessage;
        return "redirect:/ui/dashboard";
    }

    @PostMapping("/ui/dashboard/create")
    public String createChat(@RequestParam("chatTitle") String chatTitle, RedirectAttributes redirectAttributes) {
        if (chatTitle == null || chatTitle.trim().isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Chat title is required");
            return "redirect:/ui/dashboard";
        }
        String link = generateRandomLink(6);
        Chat chat = new Chat();
        chat.setTitle(chatTitle);
        chat.setLink(link);
        chatRepository.save(chat);
        return "redirect:/ui/dashboard/" + link;
    }

    private String generateRandomLink(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder();
        java.util.Random random = new java.util.Random();
        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }
}
