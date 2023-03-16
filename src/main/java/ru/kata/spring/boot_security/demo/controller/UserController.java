package ru.kata.spring.boot_security.demo.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.User;


@Controller
public class UserController {

	@GetMapping(value = "/hello")
	public String hello() {
		return "/hello";
	}
	@GetMapping(value = "/bad")
	public String bad() {
		return "/bad";
	}

	//Страница пользователя
	@GetMapping(value = "/user")
	 public String userPage(Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		User user = (User) authentication.getPrincipal();
		model.addAttribute(user);
		return "user";
	 }

}