package com.practiceproject.linkchat_back.controller;

import com.practiceproject.linkchat_back.dtos.UserEditDto;
import com.practiceproject.linkchat_back.model.User1;
import com.practiceproject.linkchat_back.repository.User1Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {

    @Autowired
    private User1Repository user1Repository;

    @GetMapping("/ui/edit-user")
    public String showEditUserForm(Model model) {
        model.addAttribute("userEditDto", new UserEditDto());
        return "edit-user";
    }

    @PostMapping("/ui/edit-user")
    public String editUser(@ModelAttribute("userEditDto") UserEditDto userEditDto) {
        User1 user = user1Repository.findByEmail(userEditDto.getEmail());
        if (user != null) {
            user.setName(userEditDto.getName());
            user.setRole(userEditDto.getRole());
            user.setActive(userEditDto.isActive());
            user1Repository.save(user);
        }
        return "redirect:/ui/dashboard";
    }
}