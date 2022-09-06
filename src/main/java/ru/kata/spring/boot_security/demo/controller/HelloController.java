package ru.kata.spring.boot_security.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import ru.kata.spring.boot_security.demo.service.UserService;

@Controller
public class HelloController {
    @GetMapping(value = "/")
    public String printWelcome(ModelMap model) {
        return "index";
    }

}

