package com.practiceproject.linkchat_back.controller;

import com.practiceproject.linkchat_back.dtos.UserEditDto;
import com.practiceproject.linkchat_back.model.User;
import com.practiceproject.linkchat_back.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/ui/edit-user")
    public String showEditUserForm(Model model) {
        model.addAttribute("userEditDto", new UserEditDto());
        return "edit-user";
    }

    @PostMapping("/ui/edit-user")
    public String editUser(@ModelAttribute("userEditDto") UserEditDto userEditDto) {
        userRepository.findById(userEditDto.getId()).ifPresent(user -> {
            user.setName(userEditDto.getName());
            user.setRole(userEditDto.getRole());
            user.setActive(userEditDto.isActive());
            userRepository.save(user);
        });
        return "redirect:/ui/dashboard";
    }

    @GetMapping("/ui/user")
    public String showUsers(Model model) {
        model.addAttribute("users", userRepository.findAll());
        return "UsersManagement";
    }
}