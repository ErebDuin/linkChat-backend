package com.practiceproject.linkchat_back.viewController;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ChatViewerController {

    @GetMapping("/ui/chat-viewer")
    public String showChatViewer(Model model) {
        return "chat-viewer";
    }

    @GetMapping("/ui/chat-viewer/chat")
    public String showChatViewerWithParams(@RequestParam(required = false) String link,
                                           @RequestParam(required = false) Long messageId,
                                           Model model) {
        if (link != null) {
            model.addAttribute("chatLink", link);
        }
        if (messageId != null) {
            model.addAttribute("messageId", messageId);
        }
        return "chat-viewer";
    }
}
