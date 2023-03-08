package ru.kata.spring.boot_security.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value ="/auth")
public class AuthController {
    @GetMapping(value ="/login")
    public String loginPage() {
        return "auth/login";
    }
}
