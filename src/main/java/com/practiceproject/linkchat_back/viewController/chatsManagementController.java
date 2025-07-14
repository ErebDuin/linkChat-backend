package com.practiceproject.linkchat_back.viewController;

import com.practiceproject.linkchat_back.model.Chat;
import com.practiceproject.linkchat_back.repository.ChatRepository;
import com.practiceproject.linkchat_back.services.ChatService;
import com.practiceproject.linkchat_back.viewModels.ChatForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/ui")
public class chatsManagementController {

    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private ChatService chatService;

    @GetMapping("/chats-management")
    public String showChatsManagement(Model model) {
        try {
            List<Chat> chats = chatRepository.findAll();
            model.addAttribute("chats", chats);
            return "chats-management";
        } catch (Exception ex) {
            model.addAttribute("errorMessage", "A system error occurred. Please try again later or contact support.");
            return "maintenance";
        }
    }

    @GetMapping("/new-chat")
    public String showChatForm(Model model) {
        ChatForm form = new ChatForm();
        form.setType(null);
        form.getInviteEmails().add("");
        model.addAttribute("chat", form);
        return "new-chat";
    }

    @PostMapping(value = "/new-chat", params = "generate")
    public String generateLink(@ModelAttribute("chat") ChatForm form, Model model) {
        chatService.generateRandomLink(form);
        model.addAttribute("chat", form);
        return "new-chat";
    }

    @PostMapping(value = "/new-chat", params = "addInvite")
    public String addInviteField(@ModelAttribute("chat") ChatForm form, Model model) {
        form.getInviteEmails().add(""); // Add an empty field
        model.addAttribute("chat", form);
        return "new-chat";
    }

    @PostMapping(value = "/new-chat", params = "removeInvite")
    public String removeInviteField(@ModelAttribute("chat") ChatForm form, Model model) {
        List<String> invites = form.getInviteEmails();
        if (!invites.isEmpty()) {
            invites.remove(invites.size() - 1); // Remove last
        }
        model.addAttribute("chat", form);
        return "new-chat";
    }


    @PostMapping(value = "/new-chat", params = "save")
    public String saveChat(@ModelAttribute("chat") ChatForm form) {
        Chat chat = new Chat();
        chat.setTitle(form.getTitle());
        chat.setLink(form.getLink());
        chat.setType(form.getType());
        chat.setActive(form.isActive());
        chat.setInviteEmails(new ArrayList<>(form.getInviteEmails())); // copy emails

        chatRepository.save(chat);

        return "redirect:/ui/chats-management";
    }
}