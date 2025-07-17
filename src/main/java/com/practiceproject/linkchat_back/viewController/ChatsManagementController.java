package com.practiceproject.linkchat_back.viewController;

import com.practiceproject.linkchat_back.dtos.ChatSettingsDto;
import com.practiceproject.linkchat_back.model.Chat;
import com.practiceproject.linkchat_back.repository.ChatRepository;
import com.practiceproject.linkchat_back.services.ChatService;
import com.practiceproject.linkchat_back.viewModels.ChatForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestParam;


import java.util.List;

@Controller
@RequestMapping("/ui")
public class ChatsManagementController {
    private static final Logger logger = LoggerFactory.getLogger(ChatsManagementController.class);

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

    @GetMapping("/chat-settings")
    public String showChatSettings(@RequestParam("id") Long id, Model model) {
        if (id == null) {
            model.addAttribute("errorMessage", "Chat ID is required.");
            return "chat-settings";
        }
        Chat chat = chatRepository.findById(id).orElse(null);
        if (chat == null) {
            model.addAttribute("errorMessage", "Chat not found.");
            return "chat-settings";
        }
        ChatSettingsDto dto = new ChatSettingsDto();
        dto.setId(chat.getChatId());
        dto.setTitle(chat.getTitle());
        dto.setLink(chat.getLink());
        dto.setType(chat.getType());
        dto.setActive(chat.isActive());
        model.addAttribute("chat", dto);
        return "chat-settings";
    }

    @PostMapping("/chat-settings")
    public String editChat(@ModelAttribute("chat") ChatSettingsDto chatSettingsDto,
                           BindingResult br, Model model) {
        if (br.hasErrors()) {
            model.addAttribute("chat", chatSettingsDto);
            return "chat-settings";
        }
        if (chatSettingsDto.getId() == null || chatSettingsDto.getTitle() == null || chatSettingsDto.getTitle().isBlank()) {
            model.addAttribute("errorMessage", "Name is required.");
            model.addAttribute("chat", chatSettingsDto);
            return "chat-settings";
        }
        Chat chat = chatRepository.findById(chatSettingsDto.getId()).orElse(null);
        if (chat == null) {
            model.addAttribute("errorMessage", "Chat not found.");
            return "chat-settings";
        }
        chat.setTitle(chatSettingsDto.getTitle());
        chat.setType(chatSettingsDto.getType());
        chat.setActive(chatSettingsDto.isActive());
        chatRepository.save(chat);
        model.addAttribute("successMessage", "Chat updated successfully!");
        model.addAttribute("chat", chatSettingsDto);
        model.addAttribute("redirectAfter", "/ui/chats-management");
        return "chat-settings";
    }

    @PostMapping(value = "/new-chat", params = "generate")
    public String generateLink(@ModelAttribute("chat") ChatForm form, Model model) {
        chatService.generateRandomLink(form);
        model.addAttribute("chat", form);
        return "new-chat";
    }

    @PostMapping(value = "/new-chat", params = "save")
    public String saveNewChat(@ModelAttribute("chat") ChatForm form) {
        chatService.saveChat(form);
        logger.info("New chat saved with title: {}", form.getTitle());
        return "redirect:/ui/chats-management";
    }
}