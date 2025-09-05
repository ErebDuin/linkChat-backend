package com.practiceproject.linkchat_back.viewController;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RootController {

    @GetMapping("/")
    public String home() {
        return "redirect:/ui/dashboard";
    }
}
