package ru.kata.spring.boot_security.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.entity.Role;
import ru.kata.spring.boot_security.demo.entity.User;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.ArrayList;
import java.util.Set;


@Controller
@RequestMapping("/admin")
public class AdminsController {
    private final UserService userService;

    public AdminsController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping()
    public String adminPage(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "foradmin";
    }

    @GetMapping("/users")
    public String index(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "users";

    }

    @GetMapping("/users/{id}")
    public String show(@PathVariable("id") long id, Model model) {
        model.addAttribute("user", userService.getUser(id));
        return "show";
    }

    @GetMapping("/users/addNew")
    public String addNew(@ModelAttribute("user") User user) {
        return "new";
    }

    @PostMapping("/users")
    public String create(@ModelAttribute("user") User user, @RequestParam(value = "role") ArrayList<Long> roles) {
        Set<Role> roleArrayList = userService.getRoles(roles);
        user.setRoles(roleArrayList);
        userService.save(user);
        return "redirect:/admin/users";
    }

    @GetMapping("/users/{id}/edit")
    public String edit(Model model, @PathVariable("id") long id) {
        model.addAttribute("user", userService.getUser(id));
        return "edit";
    }

    @PatchMapping("/users/{id}")
    public String update(@ModelAttribute("user") User user, @RequestParam(value = "role") ArrayList<Long> roles, @PathVariable("id") long id) {
        Set<Role> roleArrayList = userService.getRoles(roles);
        user.setRoles(roleArrayList);
        userService.update(user);
        return "redirect:/admin/users";
    }

    @DeleteMapping("/users/{id}/delete")
    public String delete(@PathVariable("id") long id) {
        userService.delete(id);
        return "redirect:/admin/users";
    }

}
