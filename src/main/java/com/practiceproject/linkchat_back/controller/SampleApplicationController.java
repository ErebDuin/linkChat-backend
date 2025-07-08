package com.practiceproject.linkchat_back.controller;

import com.practiceproject.linkchat_back.viewModels.SampleApplication;
import jakarta.validation.Valid;
import javassist.tools.rmi.Sample;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
public class SampleApplicationController {

    @ModelAttribute
    public SampleApplication application() {
        return new SampleApplication();
    }

    private List<SampleApplication> getSampleApplications() {
        List<SampleApplication> list = new ArrayList<>();
        for (int i = 1; i <= 20; i++) {
            SampleApplication app = new SampleApplication();
            app.setName("User" + i);
            app.setEmail("user" + i + "@mail.com");
            app.setAge(18 + i);
            app.setApproved(i % 2 == 0);
            list.add(app);
        }
        return list;
    }

//    @GetMapping("/ui/sample-application")
//    public String sampleApplication(@ModelAttribute("application") SampleApplication application, Model model) {
//        return "sample-application";
//    }

    @GetMapping("/ui/sample-application")
    public String sampleApplication(
            @ModelAttribute("application") SampleApplication application,
            Model model,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir
    ) {
        List<SampleApplication> allApps = getSampleApplications();

        // Sorting
        allApps.sort((a, b) -> {
            int cmp = 0;
            if ("name".equals(sortBy)) {
                cmp = a.getName().compareToIgnoreCase(b.getName());
            } else if ("age".equals(sortBy)) {
                cmp = a.getAge().compareTo(b.getAge());
            }
            return "asc".equals(sortDir) ? cmp : -cmp;
        });

        // Pagination
        int start = page * size;
        int end = Math.min(start + size, allApps.size());
        List<SampleApplication> pageList = allApps.subList(start, end);

        int totalPages = (int) Math.ceil((double) allApps.size() / size);

        model.addAttribute("applications", pageList);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortDir", sortDir);

        if (model.containsAttribute("approved")) {
            model.addAttribute("approved", model.getAttribute("approved"));
        }
        return "sample-application";
    }

    @PostMapping("/ui/sample-application")
    public String sampleApplication(@Valid @ModelAttribute("application") SampleApplication application,
                                     BindingResult result,
                                     RedirectAttributes redirectAttributes) {
        if (!result.hasErrors()) {
            if (application.getName().equals("Artem")) {
                application.setApproved(true);
                redirectAttributes.addFlashAttribute("approved", application.isApproved());
            }

            redirectAttributes.addFlashAttribute("message", "Application saved successfully");
            return "redirect:/ui/sample-application";
        } else {
            return "sample-application";
        }
    }
}
