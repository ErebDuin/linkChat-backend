package com.practiceproject.linkchat_back.viewController;

import com.practiceproject.linkchat_back.dtos.ChatSettingsDto;
import com.practiceproject.linkchat_back.dtos.UserEditDto;
import com.practiceproject.linkchat_back.model.Chat;
import com.practiceproject.linkchat_back.repository.ChatRepository;
import com.practiceproject.linkchat_back.services.ChatService;
import com.practiceproject.linkchat_back.viewModels.ChatForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import java.util.List;
/**
 * Controller for chats management operations.
 * Handles displaying, editing, and deleting chats via UI endpoints.
 * Uses Spring Boot, Java, and Thymeleaf for view rendering.
 *
 * Endpoints:
 * - /ui/chats-management: List all users
 * - /ui/chat-settings/save: Edit chat form and update
 * - /ui/chat-settings/delete: Delete chat
 *
 * Dependencies:
 * - ChatRepository for data access
 * - ChatSettingsDto for form binding
 */

@Controller
@RequestMapping("/ui")
public class ChatsManagementController {
    private static final Logger logger = LoggerFactory.getLogger(ChatsManagementController.class);

    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private ChatService chatService;

    /**
     * Displays the list of chats.
     * Handles database access exceptions and provides error messages.
     *
     * @param model Model to pass attributes to the view
     * @return View name for rendering
     */

    @GetMapping("/chats-management")
    public String showChatsManagement(Model model) {
        try {
            List<Chat> chats = chatRepository.findAll();
            model.addAttribute("chats", chats);
            model.addAttribute("chatSettingsDto", new ChatSettingsDto());
            return "chats-management";
        } catch (Exception ex) {
            model.addAttribute("errorMessage", "A system error occurred. Please try again later or contact support.");
            return "maintenance";
        }
    }

    /**
     * Displays the add chat form.
     * Initializes a new ChatForm with default values.
     *
     * @param model Model to pass attributes to the view
     * @return View name for rendering
     */

    @GetMapping("/new-chat")
    public String showChatForm(Model model) {
        ChatForm form = new ChatForm();
        form.setType(null);
        form.getInviteEmails().add("");
        model.addAttribute("chat", form);
        return "new-chat";
    }

    /**
     * Handles the form submission for adding a new chat.
     * Validates input and saves the new chat to the database.
     *
     * @param form  ChatForm containing the new chat data
     * @param model Model to pass attributes to the view
     * @return View name for rendering
     */

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

    /**
     * Displays the chat edit form for a specific chat.
     * Loads chat data into the form for editing.
     *
     * @param model Model to pass attributes to the view
     * @return View name for rendering
     */

    @GetMapping("/chat-settings")
    public String showChatSettings(@RequestParam("id") Long id, Model model) {
        try {
            if (id == null) {
                model.addAttribute("errorMessage", "Chat ID is required.");
                return "chat-settings";
            }
            chatRepository.findById(id).ifPresentOrElse(chat -> {
                ChatSettingsDto dto = new ChatSettingsDto();
                dto.setId(chat.getChatId());
                dto.setTitle(chat.getTitle());
                dto.setLink(chat.getLink());
                dto.setType(chat.getType());
                dto.setActive(chat.isActive());
                model.addAttribute("chatSettingsDto", dto);
            }, () -> model.addAttribute("errorMessage", "Chat not found."));
        } catch (DataAccessException ex) {
            model.addAttribute("errorMessage", "Database error.");
        }
        return "chat-settings";
    }

    /**
     * Handles the form submission for editing a chat.
     * Validates input and updates the chat in the database.
     *
     * @param chatSettingsDto DTO containing user data
     * @param br              BindingResult for validation errors
     * @param model           Model to pass attributes to the view
     * @return View name for rendering
     */

    @PostMapping("/chat-settings/save")
    public String editChat(@ModelAttribute("chatSettingsDto") ChatSettingsDto chatSettingsDto,
                           BindingResult br, Model model,
                           RedirectAttributes redirectAttributes) {
        try {
            if (br.hasErrors()) {
                model.addAttribute("chatSettingsDto", chatSettingsDto);
                return "chat-settings";
            }

            if (chatSettingsDto == null ||
                    chatSettingsDto.getId() == null ||
                    chatSettingsDto.getTitle() == null || chatSettingsDto.getTitle().isBlank()) {
                model.addAttribute("errorMessage", "Name is required.");
                model.addAttribute("chatSettingsDto", chatSettingsDto);
                return "chat-settings";
            }
            chatRepository.findById(chatSettingsDto.getId()).ifPresentOrElse(chat -> {
                chat.setTitle(chatSettingsDto.getTitle());
                chat.setType(chatSettingsDto.getType());
                chat.setActive(chatSettingsDto.isActive());
                chatRepository.save(chat);
            }, () -> {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Chat not found");
            });
            redirectAttributes.addFlashAttribute("successMessage", "Chat updated successfully!");
            return "redirect:/ui/chats-management";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error updating chat: " + e.getMessage());
            return "chat-settings";
        }
    }
}