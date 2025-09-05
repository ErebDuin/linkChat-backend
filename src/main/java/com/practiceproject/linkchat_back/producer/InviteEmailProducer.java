package com.practiceproject.linkchat_back.producer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.practiceproject.linkchat_back.producerPayloads.InviteEmailPayload;
import com.practiceproject.linkchat_back.viewModels.ChatForm;
import com.practiceproject.linkchat_back.model.InviteEmailEntry;
import com.rabbitmq.client.*;
import org.springframework.stereotype.Service;

import javax.net.ssl.SSLContext;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
public class InviteEmailProducer {

    private static final String EMAIL_QUEUE = "LinkChatQueueEmail";
    private static final String HOST = "localhost";
    private static final int PORT = 5672;
    private static final String USERNAME = "guest";
    private static final String PASSWORD = "guest";

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public void start(ChatForm form) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(HOST);
        factory.setPort(PORT);
        factory.setUsername(USERNAME);
        factory.setPassword(PASSWORD);
//        factory.useSslProtocol(SSLContext.getDefault());

        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {

            channel.queueDeclare(EMAIL_QUEUE, true, false, false, null);

            List<String> emails = form.getInviteEmails().stream()
                    .map(InviteEmailEntry::getEmail)
                    .toList();

            InviteEmailPayload payload = new InviteEmailPayload(
                    emails,
                    "You’re invited to join a chat!",
                    form.getTitle(),
                    form.getLink()
            );

            String json = objectMapper.writeValueAsString(payload);

            channel.basicPublish(
                    "",
                    EMAIL_QUEUE,
                    MessageProperties.PERSISTENT_TEXT_PLAIN,
                    json.getBytes(StandardCharsets.UTF_8)
            );
        }
    }
}
