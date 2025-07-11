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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/ui/edit-user")
    public String showEditUserForm(@RequestParam("id") Long id, Model model) {
        userRepository.findById(id).ifPresent(user -> {
            UserEditDto dto = new UserEditDto();
            dto.setId(user.getId());
            dto.setName(user.getName());
            dto.setEmail(user.getEmail());
            dto.setRole(user.getRole());
            dto.setActive(user.isActive());
            model.addAttribute("userEditDto", dto);
        });
        return "edit-user";
    }

    @PostMapping("/ui/edit-user")
    public String editUser(@ModelAttribute("userEditDto") UserEditDto userEditDto,
                           Model model) {
        userRepository.findById(userEditDto.getId()).ifPresent(user -> {
            user.setName(userEditDto.getName());
            user.setEmail(userEditDto.getEmail());
            user.setRole(userEditDto.getRole());
            user.setActive(userEditDto.isActive());
            userRepository.save(user);
        });
        model.addAttribute("userEditDto", userEditDto);
        model.addAttribute("successMessage", "User updated successfully!");
        model.addAttribute("redirectAfter", "/ui/user");
        return "edit-user";
    }

    @PostMapping("/ui/delete-user")
    public String deleteUser(@RequestParam Long id, Model model) {

        userRepository.deleteById(id);

        model.addAttribute("userEditDto", new UserEditDto());   // empty form is okay
        model.addAttribute("successMessage", "User was deleted.");
        model.addAttribute("redirectAfter", "/ui/user");        // list page
        return "edit-user";                                     // stay → toast → redirect
    }

    @GetMapping("/ui/user")
    public String showUsers(Model model) {
        model.addAttribute("users", userRepository.findAll());
        return "UsersManagement";
    }
}