package ru.kata.spring.boot_security.demo.controller;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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
public class RESTController {

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
    public RESTController(UserService userService, UserValidator userValidator, ModelMapper modelMapper) {
        this.userService = userService;
        this.userValidator = userValidator;
        this.modelMapper = modelMapper;
    }
    @CrossOrigin
    @GetMapping("/")
    public List<UserDTO> getAllUsers() {
        return userService.findAll().stream().map(this::convertToUserDTO)
                .collect(Collectors.toList());
    }
    @GetMapping("/{id}")
    public UserDTO getUserById(@PathVariable("id") Long id) {
        return convertToUserDTO(userService.findById(id));
    }
    @CrossOrigin
    @PostMapping("/")
    public ResponseEntity<HttpStatus> create(@RequestBody @Valid UserDTO userDTO, BindingResult bindingResult) {
        userValidator.validate(convertToUser(userDTO), bindingResult);
        if (bindingResult.hasErrors()) {
            throw new UserNotCreatedException(bindingResult.getFieldErrors().stream()
                    .map(error -> error.getField() + " - " + error.getDefaultMessage())
                    .collect(Collectors.joining(";")));
        }
        userService.save(convertToUser(userDTO));
        return ResponseEntity.ok(HttpStatus.OK);
    }
    @CrossOrigin
    @PatchMapping("/{id}")
    public ResponseEntity<HttpStatus> update(@RequestBody @Valid UserDTO userDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new UserNotCreatedException(bindingResult.getFieldErrors().stream()
                    .map(error -> error.getField() + " - " + error.getDefaultMessage())
                    .collect(Collectors.joining(";")));
        }
        userService.save(convertToUser(userDTO));
        return ResponseEntity.ok(HttpStatus.OK);
    }
    @CrossOrigin
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<HttpStatus> delete(Model model, @PathVariable("id") Long id) {
        userService.deleteById(id);
        return ResponseEntity.ok(HttpStatus.OK);
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