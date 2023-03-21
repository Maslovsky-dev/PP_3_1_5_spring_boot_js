package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;
import ru.kata.spring.boot_security.demo.util.UserValidator;

import javax.validation.Valid;


@Controller
public class AdminController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final UserValidator userValidator;

    @Autowired
    public AdminController(UserService userService, PasswordEncoder passwordEncoder, UserValidator userValidator) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.userValidator = userValidator;
    }
    private void addDataToModel(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        model.addAttribute("authUser", user);
        model.addAttribute("allUsers", userService.findAll());
    }

    // Запрос на вход в админку (только для пользователей с ролью ADMIN)
    @GetMapping("/admin")
    public String adminPage(Model model) {
        addDataToModel(model);
        model.addAttribute("newUser", new User());
        model.addAttribute("allUsersTab", "tab-pane fade show active");
        model.addAttribute("addNewUserTab", "tab-pane fade");
        return "admin";
    }

    //Обработка запроса на создание нового пользователя, валидация введенных данных
    @PostMapping("/registration")
    public String performRegistration(Model model, @ModelAttribute("newUser") @Valid User user, BindingResult bindingResult) {
        userValidator.validate(user, bindingResult);
        if (bindingResult.hasErrors()) {
            addDataToModel(model);
            model.addAttribute("newUser", user);
            model.addAttribute("allUsersTab", "tab-pane fade");
            model.addAttribute("addNewUserTab", "tab-pane fade show active");
            return "admin";
        }
        userService.save(user);
        return "redirect:/admin";
    }

    @GetMapping(value = "/{id}/edit")
    public String edit(Model model, @PathVariable("id") Long id) {
        model.addAttribute("editUser", userService.findById(id).get());
        return "editModal";
    }

    // Обработка запроса на изменение данных пользователя (только для пользователей с ролью ADMIN)
    @PatchMapping("/{id}")
    public String update(@ModelAttribute("editUser") @Valid User user,
                         BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "editModal";
        userService.save(user);
        return "redirect:/admin";
    }

    // Обработка запроса на удаление пользователя (только для пользователей с ролью ADMIN)
    @GetMapping(value = "/{id}/delete")
    public String delete(Model model, @PathVariable("id") Long id) {
        model.addAttribute("deleteUser", userService.findById(id).get());
        return "deleteModal";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") Long id) {
        userService.deleteById(id);
        return "redirect:/admin";
    }

}