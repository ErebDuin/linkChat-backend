package com.practiceproject.linkchat_back.viewController;

import com.practiceproject.linkchat_back.model.Chat;
import com.practiceproject.linkchat_back.model.InviteEmailEntry;
import com.practiceproject.linkchat_back.repository.ChatRepository;
import com.practiceproject.linkchat_back.services.ChatService;
import com.practiceproject.linkchat_back.viewModels.ChatForm;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/ui")
public class ChatsManagementController {
    private static final Logger logger = LoggerFactory.getLogger(ChatsManagementController.class);

    private final ChatRepository chatRepository;
    private final ChatService chatService;

    public ChatsManagementController(ChatRepository chatRepository, ChatService chatService) {
        this.chatRepository = chatRepository;
        this.chatService = chatService;
    }

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
        if (!model.containsAttribute("chat")) {
            model.addAttribute("chat", chatService.createDefaultChatForm());
        }
        return "new-chat";
    }

    @PostMapping(value = "/new-chat", params = "generate")
    public String generateLink(@ModelAttribute("chat") ChatForm form, RedirectAttributes redirectAttributes) {
        chatService.generateRandomLink(form);
        redirectAttributes.addFlashAttribute("chat", form);
        logger.info("::Generated new chat link: {}", form.getLink());
        return "redirect:/ui/new-chat";
    }

    @PostMapping(value = "/new-chat", params = "save")
    public String saveNewChat(@Valid @ModelAttribute("chat") ChatForm form, BindingResult result) {
        if (result.hasErrors()) {
            logger.warn("::Validation errors: {}", result.getAllErrors());
            return "new-chat";
        }

        chatService.addInviteEmail(form, null);
        logger.info("::Adding invite emails: {}", form.getInviteEmails());
        chatService.saveChat(form);
        logger.info("::New chat saved with title: {}", form.getTitle());
        return "redirect:/ui/chats-management";
    }
}