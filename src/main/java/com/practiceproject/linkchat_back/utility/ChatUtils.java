package com.practiceproject.linkchat_back.utility;

import java.util.UUID;

public class ChatUtils {
    public static String generateRandomChatLink() {
        return "/chat/" + UUID.randomUUID().toString().substring(0, 16).replace("-", "");
    }
}
