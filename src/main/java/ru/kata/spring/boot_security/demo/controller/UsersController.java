package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;
import ru.kata.spring.boot_security.demo.service.UserService;

@Controller
public class UsersController {

    @GetMapping("/user")
    public String forUsers(ModelAndView modelAndView){
        return"foruser";
    }
}
