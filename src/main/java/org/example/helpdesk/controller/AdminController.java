package org.example.helpdesk.controller;

import org.example.helpdesk.entity.Role;
import org.example.helpdesk.entity.User;
import org.example.helpdesk.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/users")
public class AdminController {
    private final UserRepository users;
    private final PasswordEncoder passwordEncoder;

    public AdminController(UserRepository users, PasswordEncoder passwordEncoder) {
        this.users = users;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("users", users.findAll());
        return "admin/users";
    }

    @GetMapping("/new")
    public String newUser(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("roles", Role.values());
        return "admin/user-form";
    }

    @PostMapping
    public String create(@RequestParam String username,
                         @RequestParam String password,
                         @RequestParam Role role) {
        User user = new User(username, passwordEncoder.encode(password), role);
        users.save(user);
        return "redirect:/admin/users";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id,
                         @RequestParam Role role,
                         @RequestParam(defaultValue = "false") boolean enabled) {
        User user = users.findById(id).orElseThrow();
        user.setRole(role);
        user.setEnabled(enabled);
        users.save(user);
        return "redirect:/admin/users";
    }
}
