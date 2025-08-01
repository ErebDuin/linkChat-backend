package com.practiceproject.linkchat_back.viewController;

import com.practiceproject.linkchat_back.dtos.ChatSettingsDto;
import com.practiceproject.linkchat_back.dtos.SimpleEmailRequest;
import com.practiceproject.linkchat_back.model.Chat;
import com.practiceproject.linkchat_back.model.InviteEmailEntry;
import com.practiceproject.linkchat_back.repository.ChatRepository;
import com.practiceproject.linkchat_back.services.ChatService;
import com.practiceproject.linkchat_back.services.EmailService;
import com.practiceproject.linkchat_back.viewModels.ChatForm;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
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

import java.io.IOException;
import java.util.List;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

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

    private final ChatRepository chatRepository;
    private final ChatService chatService;
    private final EmailService emailService;

    public ChatsManagementController(ChatRepository chatRepository, ChatService chatService, EmailService emailService) {
        this.chatRepository = chatRepository;
        this.chatService = chatService;
        this.emailService = emailService;
    }

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
        if (!model.containsAttribute("chat")) {
            model.addAttribute("chat", chatService.createDefaultChatForm());
        }
        return "new-chat";
    }

    /**
     * Handles the form submission when the "Generate" button is clicked.
     * Generates a random chat link and adds it to the form.
     *
     * @param form ChatForm containing current user input
     * @param redirectAttributes RedirectAttributes to persist the form across redirect
     * @return Redirects back to the new chat form view with updated form data
     */

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

    @PostMapping(value = "/new-chat", params = "save-n-send")
    public String saveAndSendNewChat(
            @Valid @ModelAttribute("chat") ChatForm form,
            BindingResult result,
            @ModelAttribute("emailRequest") SimpleEmailRequest emailRequest
    ) throws MessagingException, IOException {
        if (result.hasErrors()) {
            logger.warn("::Validation error: {}", result.getAllErrors());
            return "new-chat";
        }

        chatService.saveChat(form);
        emailService.sendInviteEmail(emailRequest, form);
        logger.info("::New chat saved and invite email sent to: {}", emailRequest.getTo());
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
    public String showChatSettings(@RequestParam("id") Long chatId, Model model) {
        try {
            Chat chat = chatRepository.findById(chatId)
                    .orElseThrow(() -> new IllegalArgumentException("Chat not found with ID: " + chatId));

            ChatSettingsDto dto = new ChatSettingsDto();
            dto.setId(chat.getChatId());
            dto.setTitle(chat.getTitle());
            dto.setLink(chat.getLink());
            dto.setType(chat.getType());
            dto.setActive(chat.isActive());

            model.addAttribute("chatSettingsDto", dto);
            return "chat-settings";
        } catch (Exception ex) {
            logger.error("::Failed to load chat settings", ex);
            model.addAttribute("errorMessage", "A system error occurred. Please try again later or contact support.");
            return "maintenance";
        }
    }

    /**
     * Handles the form submission for editing a chat.
     * Validates input and updates the chat in the database.
     *
     * @param chatSettingsDto DTO containing user data
     * @param result              BindingResult for validation errors
     * @param model           Model to pass attributes to the view
     * @return View name for rendering
     */

    @PostMapping("/chat-settings/save")
    public String editChatSettings(@Valid @ModelAttribute("chatSettingsDto") ChatSettingsDto chatSettingsDto,
                                   BindingResult result,
                                   Model model) {
        if (result.hasErrors()) {
            logger.warn("::Validation errors in chat settings: {}", result.getAllErrors());
            return "chat-settings";
        }

        try {
            chatService.updateChatSettings(chatSettingsDto);
            logger.info("::Chat settings updated for Chat ID: {}", chatSettingsDto.getId());
            return "redirect:/ui/chats-management";
        } catch (Exception ex) {
            logger.error("::Failed to update chat settings", ex);
            model.addAttribute("errorMessage", "A system error occurred while saving settings. Please try again.");
            return "chat-settings";
        }
    }

     /**
     * Handles the deletion of a chat by its ID.
     * On success, redirects to the chats management page.
     * On failure, logs the error, sets an error message, and returns to the chat settings page.
     *
     * @param chatId The ID of the chat to delete (from request parameter)
     * @param model  Model to pass attributes to the view
     * @return Redirect or view name
     */

      @PostMapping("/chat-settings/delete")
      public String deleteChat(@RequestParam("id") Long chatId, Model model) {
        try {
        chatService.deleteChatById(chatId);
        logger.info("::Chat deleted with ID: {}", chatId);
        return "redirect:/ui/chats-management";
         } catch (Exception ex) {
        logger.error("::Failed to delete chat with ID: {}", chatId, ex);
        model.addAttribute("errorMessage", "A system error occurred while deleting the chat.");
        return "chat-settings";
      }
   }
}
