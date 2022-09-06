package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.entity.Role;
import ru.kata.spring.boot_security.demo.entity.User;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Set;


@Controller
@RequestMapping("/admin")
public class AdminsController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AdminsController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }


    @GetMapping()
    public String adminPage(Principal principal, Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "foradmin";
    }


    @GetMapping("/users/{id}")
    public String show(@PathVariable("id") long id, Model model) {
        model.addAttribute("user", userService.getUser(id));
        return "foruser";
    }

    @GetMapping("/users/addNew")
    public String addNew(@ModelAttribute("user") User user) {
        return "new";
    }

    @PostMapping()
    public String create(@ModelAttribute("user") User user, @RequestParam(value = "role") ArrayList<Long> roles) {
        Set<Role> roleArrayList = userService.getRoles(roles);
        user.setRoles(roleArrayList);
        userService.save(user);
        return "redirect:/admin/";
    }

    @PostMapping(value = "/users/{id}")
    public String saveUpdate(@ModelAttribute("user") User user, @RequestParam(value = "role") ArrayList<Long> roles, @PathVariable("id") int id) {
        Set<Role> roleArrayList = userService.getRoles(roles);
        user.setRoles(roleArrayList);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userService.update(id, user);
        return "redirect:/admin";
    }

    @DeleteMapping("/users/{id}/delete")
    public String delete(@PathVariable("id") long id) {
        userService.delete(id);
        return "redirect:/admin";
    }

}
