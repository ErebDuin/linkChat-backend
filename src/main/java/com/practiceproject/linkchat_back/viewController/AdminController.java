package com.practiceproject.linkchat_back.viewController;

import com.practiceproject.linkchat_back.dtos.AdminRegistrationDto;
import com.practiceproject.linkchat_back.exceptions.EmailAlreadyExistsException;
import com.practiceproject.linkchat_back.exceptions.PasswordMismatchException;
import com.practiceproject.linkchat_back.services.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/ui")
public class AdminController {
    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

    @Autowired
    private UserService userService;

    /**
     * Displays the admin login page.
     *
     * @return View name for admin login
     */
    @GetMapping("/admin-login")
    public String login() {
        logger.debug("Accessing admin login page");
        return "admin-login";
    }

    /**
     * Displays the admin registration page.
     *
     * @return View name for admin registration
     */
    @GetMapping("/admin-register")
    public String register() {
        logger.debug("Accessing admin registration page");
        return "admin-register";
    }

    /**
     * Handles admin registration form submission.
     * Validates input and manages exceptions for password mismatch and email duplication.
     *
     * @param registrationDto Data transfer object for admin registration
     * @param result         BindingResult for validation errors
     * @param model          Model to pass attributes to the view
     * @return Redirects to login on success, or returns to registration on error
     */
    @PostMapping("/admin-register")
    public String registerAdmin(
            @ModelAttribute @Valid AdminRegistrationDto registrationDto,
            BindingResult result,
            Model model
    ) {
        if (result.hasErrors()) {
            return "admin-register";
        }
        try {
            userService.saveAdminUser(registrationDto);
            model.addAttribute("successMessage", "Admin registered successfully!");
            logger.debug("Admin user registered successfully: {}", registrationDto.getUsername());
            return "redirect:/ui/admin-login";
        } catch (PasswordMismatchException | EmailAlreadyExistsException e) {
            model.addAttribute("errorMessage", e.getMessage());
            logger.error("Error during admin registration: {}", e.getMessage());
            return "admin-register";
        }
    }
}
