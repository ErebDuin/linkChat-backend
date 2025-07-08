package com.practiceproject.linkchat_back.controller;
import com.practiceproject.linkchat_back.model.ChatMessage1;
import com.practiceproject.linkchat_back.services.ChatMessage1Service;
import com.practiceproject.linkchat_back.viewModels.ChatMessage1ViewModel;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.validation.Valid;

@Controller
public class ChatMessage1Controller {

    private final ChatMessage1Service service;

    public ChatMessage1Controller(ChatMessage1Service service) {
        this.service = service;
    }

    @GetMapping("/ui/chatMessages1")
    public String chatMessages1(Model model) {
        model.addAttribute("chatMessages", service.findAll());
        model.addAttribute("chatMessageForm", new ChatMessage1ViewModel());
        return "chatMessages1";
    }

    @PostMapping("/ui/chatMessages1/add")
    public String addChatMessage(@Valid @ModelAttribute("chatMessageForm") ChatMessage1ViewModel chatMessageViewModel,
                                BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("chatMessages", service.findAll());
            return "chatMessages1";
        }

        ChatMessage1 chatMessage = convertToEntity(chatMessageViewModel);
        service.save(chatMessage);
        return "redirect:/ui/chatMessages1";
    }

    @GetMapping("/ui/chatMessages1/delete/{id}")
    public String deleteChatMessage(@PathVariable Long id) {
        service.deleteById(id);
        return "redirect:/ui/chatMessages1";
    }

    @PostMapping("/ui/chatMessages1/update")
    public String updateChatMessage(@Valid @ModelAttribute("chatMessageForm") ChatMessage1ViewModel chatMessageViewModel,
                                  BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("chatMessages", service.findAll());
            return "chatMessages1";
        }

        ChatMessage1 chatMessage = convertToEntity(chatMessageViewModel);
        service.save(chatMessage);
        return "redirect:/ui/chatMessages1";
    }

    private ChatMessage1 convertToEntity(ChatMessage1ViewModel viewModel) {
        ChatMessage1 entity = new ChatMessage1();
        entity.setMessage_id(viewModel.getMessage_id());
        entity.setRecipient(viewModel.getRecipient());
        entity.setSender(viewModel.getSender());
        entity.setTimestamp(viewModel.getTimestamp());
        return entity;
    }
}
