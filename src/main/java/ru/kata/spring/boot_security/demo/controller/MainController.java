package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.security.UserDetails;


@Controller
public class MainController {
//	@Autowired
//	UserService userService;
	//Отображение всех пользователей
//	@GetMapping(value = "/")
//	public String printWelcome(Model model) {
//		model.addAttribute("allUsers", userService.listUsers());
//		return "index";
//	}
//
//	@GetMapping(value = "/new")
//	public String addNewUser(@ModelAttribute ("user") User user) {
//		return "new";
//	}
//	//Сохранение пользователя
//	@PostMapping("/saveUser")
//	public String saveUser(@ModelAttribute ("user") User user) {
//		userService.add(user);
//		return "redirect:/";
//	}
//	//Форма для редактирования пользователя
//	@GetMapping (value = "/{id}/edit")
//	public String edit(Model model, @PathVariable("id") Long id) {
//		model.addAttribute("user",userService.userById(id));
//		return "edit";
//	}
//	@PatchMapping("/{id}")
//	public  String update(@ModelAttribute("user") User user) {
//		System.out.println("Controller: " + user);
//		userService.update(user);
//		return "redirect:/";
//	}
//	@DeleteMapping("/{id}")
//	public String delete(@PathVariable("id") Long id) {
//		userService.delete(id);
//		return "redirect:/";
//	}
	@GetMapping(value = "/hello")
	 public String sayHello() {
		return "hello";
	 }
	 @GetMapping(value = "/showUserInfo")
	public String showUserInfo() {
		 Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		 UserDetails userDetails = (UserDetails)authentication.getPrincipal();
		 System.out.println(userDetails.getUser());
		return "hello";
	 }
}