package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.security.UserDetailsImpl;
import ru.kata.spring.boot_security.demo.service.UserService;

import javax.validation.Valid;


@Controller
public class MainController {

	private final UserService userService;

	@Autowired
	public MainController(UserService userService) {
		this.userService = userService;
	}
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
		UserDetailsImpl userDetailsImpl = (UserDetailsImpl)authentication.getPrincipal();
		model.addAttribute(userDetailsImpl.getUser());
		return "user";
	 }

	 // Запрос на вход в админку (только для пользователей с ролью ADMIN)
	 @GetMapping("/admin")
	public String adminPage(Model model) {
		model.addAttribute("allUsers",userService.listUsers());
		return "new_admin";
	 }
	//Форма для редактирования пользователя (только для пользователей с ролью ADMIN)
	@GetMapping (value = "/{id}/edit")
	public String edit(Model model, @PathVariable("id") Long id) {
		model.addAttribute("user",userService.userById(id));
		return "edit";
	}
	// Обработка запроса на изменение данных пользователя (только для пользователей с ролью ADMIN)
	@PatchMapping("/{id}")
	public  String update(@ModelAttribute("user") @Valid User user,
						  BindingResult bindingResult) {
		if (bindingResult.hasErrors())
			return "/edit";
		userService.update(user);
		return "redirect:/admin";
	}
	// Обработка запроса на удаление пользователя (только для пользователей с ролью ADMIN)
	@DeleteMapping("/{id}")
	public String delete(@PathVariable("id") Long id) {
		userService.delete(id);
		return "redirect:/admin";
	}

}