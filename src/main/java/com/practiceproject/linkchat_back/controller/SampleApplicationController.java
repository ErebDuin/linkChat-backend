package com.practiceproject.linkchat_back.controller;

import com.practiceproject.linkchat_back.viewModels.SampleApplication;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class SampleApplicationController {

    @GetMapping("/ui/sample-application")
    public String sampleApplication(Model model) {
        SampleApplication app = new SampleApplication();
        model.addAttribute("application", app);
        return "sample-application";
    }

    @PostMapping("/ui/sample-application")
    public String sampleApplication(@Valid @ModelAttribute("application") SampleApplication application,
                                     BindingResult result,
                                     RedirectAttributes redirectAttributes) {
        SampleApplication app = application;
        BindingResult bindingResult = result;
        if (!result.hasErrors()) {
            // save to the database
        } else {
            // handle validation errors
        }

        return "sample-application";
    }
}
