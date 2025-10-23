package com.swapshelf.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomePageController {

    @GetMapping("/")
    public String home(final Model model) {
        return "index";
    }

    @GetMapping("/help")
    public String helpPage() {
        return "help";

    }
}
