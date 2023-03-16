package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RegistrationService;
import ru.kata.spring.boot_security.demo.util.UserValidator;

import javax.validation.Valid;

@Controller
@RequestMapping(value = "/auth")
public class AuthController {
    private final UserValidator userValidator;
    private final RegistrationService registrationService;

    @Autowired
    public AuthController(UserValidator userValidator, RegistrationService registrationService) {
        this.userValidator = userValidator;
        this.registrationService = registrationService;
    }

    //Запрос на форму для входа в приложение
    @GetMapping(value = "/login")
    public String loginPage() {
        return "auth/login";
    }

    // Запрос на форму для регистрации нового пользователя
    @GetMapping("/registration")
    public String registrationPage(@ModelAttribute("user") User user) {
        return "auth/registration";
    }

    //Обработка запроса на создание нового пользователя, валидация введенных данных
    @PostMapping("/registration")
    public String performRegistration(@ModelAttribute("newUser") @Valid User user, BindingResult bindingResult) {
        System.out.println(user);
        userValidator.validate(user, bindingResult);
        if (bindingResult.hasErrors()) {
            System.out.println(bindingResult.toString());
            return "redirect:/admin";
        }
        registrationService.register(user);
        return "redirect:/admin";
    }


}
