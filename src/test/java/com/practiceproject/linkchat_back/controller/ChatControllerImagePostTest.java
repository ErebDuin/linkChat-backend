package com.practiceproject.linkchat_back.controller;

import com.practiceproject.linkchat_back.model.Chat;
import com.practiceproject.linkchat_back.model.ChatMessage;
import com.practiceproject.linkchat_back.repository.ChatMessageRepository;
import com.practiceproject.linkchat_back.repository.ChatRepository;
import com.practiceproject.linkchat_back.repository.ChatUserRepository;
import com.practiceproject.linkchat_back.repository.ChatSettingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class ChatControllerImagePostTest {

    private MockMvc mockMvc;

    @Mock
    private ChatRepository chatRepository;

    @Mock
    private ChatMessageRepository chatMessageRepository;

    @Mock
    private ChatUserRepository chatUserRepository;

    @Mock
    private ChatSettingRepository chatSettingRepository;

    private ChatController chatController;

    private final String BASE64_IMAGE = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAvcAAALhCAYAAADb8Is9AAAMTGlDQ1BJQ0MgUHJvZmlsZQAASImVVwdYU8kWnltSIQQIREBK6E0QkRJASggtgPQiiEpIAoQSY0JQsaOLCq5dRLCiqyCKHRCxYVcWxe5aFgsqK+tiwa68CQF02Ve+N983d/77z5l";

    @BeforeEach
    void setUp() {
        chatController = new ChatController(
            chatRepository,
            chatUserRepository,
            chatMessageRepository,
            chatSettingRepository
        );

        mockMvc = MockMvcBuilders
            .standaloneSetup(chatController)
            .build();
    }

    @Test
    void testSendImageMessageSuccess() throws Exception {
        Chat chat = new Chat();
        chat.setChatId(1L);
        chat.setLink("0GU8kb");
        Mockito.when(chatRepository.findByLink("0GU8kb")).thenReturn(Optional.of(chat));
        Mockito.when(chatMessageRepository.save(any(ChatMessage.class))).thenAnswer(invocation -> {
            ChatMessage msg = invocation.getArgument(0);
            msg.setMessageId(100L);
            return msg;
        });

        String json = "{" +
                "\"sender\": \"test5@gmail.com\"," +
                "\"recipient\": \"test6@gmail.com\"," +
                "\"imageBase64\": \"" + BASE64_IMAGE + "\"," +
                "\"filename\": \"test.png\"," +
                "\"contentType\": \"image/png\"}";

        mockMvc.perform(post("/api/chat/0GU8kb/image")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Image message sent"));

        ArgumentCaptor<ChatMessage> captor = ArgumentCaptor.forClass(ChatMessage.class);
        Mockito.verify(chatMessageRepository).save(captor.capture());
        ChatMessage saved = captor.getValue();
        assertThat(saved.getImageData()).isEqualTo(BASE64_IMAGE);
        assertThat(saved.getImageFilename()).isEqualTo("test.png");
        assertThat(saved.getImageContentType()).isEqualTo("image/png");
        assertThat(saved.getSender()).isEqualTo("test5@gmail.com");
        assertThat(saved.getRecipient()).isEqualTo("test6@gmail.com");
        assertThat(saved.getMessageType()).isEqualTo(ChatMessage.MessageType.IMAGE);
    }

    @Test
    void testSendImageMessageChatNotFound() throws Exception {
        Mockito.when(chatRepository.findByLink("0GU8kb")).thenReturn(Optional.empty());
        String json = "{" +
                "\"sender\": \"test5@gmail.com\"," +
                "\"recipient\": \"test6@gmail.com\"," +
                "\"imageBase64\": \"" + BASE64_IMAGE + "\"," +
                "\"filename\": \"test.png\"," +
                "\"contentType\": \"image/png\"}";
        mockMvc.perform(post("/api/chat/0GU8kb/image")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Chat not found"));
    }

    @Test
    void testSendImageMessageInvalidBase64() throws Exception {
        Chat chat = new Chat();
        chat.setChatId(1L);
        chat.setLink("0GU8kb");
        Mockito.when(chatRepository.findByLink("0GU8kb")).thenReturn(Optional.of(chat));
        String invalidBase64 = "not_base64_data";
        String json = "{" +
                "\"sender\": \"test5@gmail.com\"," +
                "\"recipient\": \"test6@gmail.com\"," +
                "\"imageBase64\": \"" + invalidBase64 + "\"," +
                "\"filename\": \"test.png\"," +
                "\"contentType\": \"image/png\"}";
        mockMvc.perform(post("/api/chat/0GU8kb/image")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Invalid image format"));
    }
}
