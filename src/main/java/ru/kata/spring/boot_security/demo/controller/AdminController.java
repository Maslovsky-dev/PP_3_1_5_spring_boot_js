package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repository.UserRepository;

import javax.validation.Valid;


@Controller
public class AdminController {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	@Autowired
	public AdminController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@GetMapping(value = "/hello")
	public String hello() {
		return "/hello";
	}
	@GetMapping(value = "/bad")
	public String bad() {
		return "/bad";
	}


	 // Запрос на вход в админку (только для пользователей с ролью ADMIN)
	 @GetMapping("/admin")
	public String adminPage(Model model) {
		model.addAttribute("allUsers",userRepository.findAll());
		return "new_admin";
	 }
	//Форма для редактирования пользователя (только для пользователей с ролью ADMIN)
	@GetMapping (value = "/{id}/edit")
	public String edit(Model model, @PathVariable("id") Long id) {
		model.addAttribute("user",userRepository.findById(id).get());
		return "edit";
	}
	// Обработка запроса на изменение данных пользователя (только для пользователей с ролью ADMIN)
	@PatchMapping("/{id}")
	public  String update(@ModelAttribute("user") @Valid User user,
						  BindingResult bindingResult) {
		if (bindingResult.hasErrors())
			return "/edit";
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		userRepository.save(user);
		return "redirect:/admin";
	}
	// Обработка запроса на удаление пользователя (только для пользователей с ролью ADMIN)
	@DeleteMapping("/{id}")
	public String delete(@PathVariable("id") Long id) {
		userRepository.deleteById(id);
		return "redirect:/admin";
	}

}