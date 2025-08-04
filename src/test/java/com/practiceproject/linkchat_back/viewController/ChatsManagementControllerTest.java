package com.practiceproject.linkchat_back.viewController;

import com.practiceproject.linkchat_back.dtos.SimpleEmailRequest;
import com.practiceproject.linkchat_back.model.Chat;
import com.practiceproject.linkchat_back.services.ChatService;
import com.practiceproject.linkchat_back.services.EmailService;
import com.practiceproject.linkchat_back.viewModels.ChatForm;
import com.practiceproject.linkchat_back.repository.ChatRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class ChatsManagementControllerTest {

    @Mock
    private ChatService chatService;

    @Mock
    private EmailService emailService;

    @Mock
    private RedirectAttributes redirectAttributes;

    @Mock
    private SimpleEmailRequest request;

    @InjectMocks
    private ChatsManagementController controller;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void showChatForm_ShouldAddChatFormToModel_WhenNotPresent() {
        // Arrange
        Model model = new ConcurrentModel();
        ChatForm mockForm = new ChatForm();
        when(chatService.createDefaultChatForm()).thenReturn(mockForm);

        // Act
        String view = controller.showChatForm(model);

        // Assert
        assertEquals("new-chat", view);
        assertTrue(model.containsAttribute("chat"));
        assertEquals(mockForm, model.getAttribute("chat"));
    }

    @Test
    void showChatForm_ShouldNotOverwriteChatForm_WhenAlreadyPresent() {
        // Arrange
        Model model = new ConcurrentModel();
        ChatForm existingForm = new ChatForm();
        model.addAttribute("chat", existingForm);

        // Act
        String view = controller.showChatForm(model);

        // Assert
        assertEquals("new-chat", view);
        assertEquals(existingForm, model.getAttribute("chat"));
        verify(chatService, never()).createDefaultChatForm();
    }

    @Test
    void testGenerateLink() {
        // Given
        ChatForm form = new ChatForm();

        // When
        String view = controller.generateLink(form, redirectAttributes);

        // Then
        verify(chatService, times(1)).generateRandomLink(form);
        verify(redirectAttributes, times(1)).addFlashAttribute("chat", form);
        assertEquals("redirect:/ui/new-chat", view);
    }

    @Test
    void saveNewChat_ShouldSaveChatAndRedirect_WhenNoValidationErrors() {
        // Arrange
        ChatForm form = new ChatForm();
        form.setTitle("Test Chat");
        form.setLink("link123");
        // Set other fields if needed

        BindingResult bindingResult = Mockito.mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(false);  // no validation errors

        // Act
        String view = controller.saveNewChat(form, bindingResult);

        // Assert
        verify(chatService).addInviteEmail(eq(form), isNull());
        verify(chatService).saveChat(form);
        assertEquals("redirect:/ui/chats-management", view);
    }

    @Test
    void saveNewChat_ShouldReturnFormView_WhenValidationErrorsExist() {
        // Arrange
        ChatForm form = new ChatForm();
        BindingResult bindingResult = Mockito.mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(true);  // simulate validation errors

        // Act
        String view = controller.saveNewChat(form, bindingResult);

        // Assert
        verify(chatService, never()).saveChat(any());
        assertEquals("new-chat", view);
    }

    @Test
    void saveAndSendNewChat_ShouldSaveChatAndSendEmailAndRedirect() throws Exception {
        // Arrange
        ChatForm form = new ChatForm();
        form.setLink("abc123");
        // set other fields if needed

        SimpleEmailRequest emailRequest = new SimpleEmailRequest();
        emailRequest.setTo("user@example.com");
        // set other fields if needed

        BindingResult bindingResult = Mockito.mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(false);  // simulate no validation errors

        // Act
        String view = controller.saveAndSendNewChat(form, bindingResult, emailRequest);

        // Assert
        verify(chatService).saveChat(form);
        verify(emailService).sendInviteEmail(emailRequest, form);
        assertEquals("redirect:/ui/chats-management", view);
    }
}