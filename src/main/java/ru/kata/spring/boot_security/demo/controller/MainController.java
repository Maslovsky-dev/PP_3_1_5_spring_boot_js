package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.security.UserDetails;
import ru.kata.spring.boot_security.demo.service.UserService;


@Controller
public class MainController {
	@Autowired
	private UserService userService;
	public MainController(UserService userService) {
		this.userService = userService;
	}

	//Страница пользователя, на ней должны быть данные пользователя TODO
	@GetMapping(value = "/user")
	 public String userPage(Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = (UserDetails)authentication.getPrincipal();
		model.addAttribute(userDetails.getUser());
		return "user";
	 }

	 // Запрос на вход в админку (только для пользователей с ролью ADMIN)
	 @GetMapping("/admin")
	public String adminPage(Model model) {
		model.addAttribute("allUsers",userService.listUsers());
		return "admin";
	 }
	//Форма для редактирования пользователя (только для пользователей с ролью ADMIN) TODO
	@GetMapping (value = "/{id}/edit")
	public String edit(Model model, @PathVariable("id") Long id) {
		model.addAttribute("user",userService.userById(id));
		return "edit";
	}
	// Обработка запроса на изменение данных пользователя (только для пользователей с ролью ADMIN) TODO
	@PatchMapping("/{id}")
	public  String update(@ModelAttribute("user") User user) {
		System.out.println("Controller: " + user);
		userService.update(user);
		return "redirect:/admin";
	}
	// Обработка запроса на удаление пользователя (только для пользователей с ролью ADMIN) TODO
	@DeleteMapping("/{id}")
	public String delete(@PathVariable("id") Long id) {
		userService.delete(id);
		return "redirect:/admin";
	}
}