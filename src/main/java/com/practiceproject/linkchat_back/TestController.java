package com.practiceproject.linkchat_back;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/sample")
public class TestController {

    @GetMapping
    public String getSampleMessage() {
        return "Test endpoint";
    }
}
