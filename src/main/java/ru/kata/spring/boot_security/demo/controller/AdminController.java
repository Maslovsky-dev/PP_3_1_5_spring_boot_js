package ru.kata.spring.boot_security.demo.controller;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.dto.UserDTO;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;
import ru.kata.spring.boot_security.demo.util.UserErrorResponse;
import ru.kata.spring.boot_security.demo.util.UserNotCreatedException;
import ru.kata.spring.boot_security.demo.util.UserNotFoundException;
import ru.kata.spring.boot_security.demo.util.UserValidator;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/users")
public class AdminController {

    private final UserService userService;
    private final UserValidator userValidator;
    private final ModelMapper modelMapper;
    private User convertToUser(UserDTO userDTO) {
        return modelMapper .map(userDTO, User.class);
    }
    private UserDTO convertToUserDTO(User user) {
        return modelMapper.map(user, UserDTO.class);
    }

    @Autowired
    public AdminController(UserService userService, UserValidator userValidator, ModelMapper modelMapper) {
        this.userService = userService;
        this.userValidator = userValidator;
        this.modelMapper = modelMapper;
    }
    private void addDataToModel(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        model.addAttribute("authUser", user);
        model.addAttribute("allUsers", userService.findAll());
    }
    @GetMapping("/")
    public List<UserDTO> getAllUsers() {

        return userService.findAll().stream().map(this::convertToUserDTO)
                .collect(Collectors.toList());
    }
    @GetMapping("/{id}")
    public UserDTO getUserById(@PathVariable("id") Long id) {
        return convertToUserDTO(userService.findById(id));
    }
    @PostMapping
    public ResponseEntity<HttpStatus> create(@RequestBody @Valid UserDTO userDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            StringBuilder errorMsg = new StringBuilder();
            List<FieldError> errors = bindingResult.getFieldErrors();
            for (FieldError error: errors) {
                errorMsg.append(error.getField())
                        .append(" - ").append(error.getDefaultMessage())
                        .append(";");
            }
            throw new UserNotCreatedException(errorMsg.toString());
        }
        userService.save(convertToUser(userDTO));
        return ResponseEntity.ok(HttpStatus.OK);
    }
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
        model.addAttribute("editUser", userService.findById(id));
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
        model.addAttribute("deleteUser", userService.findById(id));
        return "deleteModal";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") Long id) {
        userService.deleteById(id);
        return "redirect:/admin";
    }

    @ExceptionHandler
    private ResponseEntity<UserErrorResponse> handleException(UserNotFoundException e) {
        UserErrorResponse response = new UserErrorResponse(
                "User with this id wasn't found",
                System.currentTimeMillis()
        );
        // В HTTP ответе тело ответа (response) и статус в заголовке
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND); //NOT FOUND - 404
    }
    @ExceptionHandler
    private ResponseEntity<UserErrorResponse> handleException(UserNotCreatedException e) {
        UserErrorResponse response = new UserErrorResponse(
                e.getMessage(),
                System.currentTimeMillis()
        );
        // В HTTP ответе тело ответа (response) и статус в заголовке
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST); //NOT FOUND - 404
    }

}