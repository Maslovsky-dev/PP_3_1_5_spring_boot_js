package ru.kata.spring.boot_security.demo.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserDetailsServiceImpl;

@Component
public class UserValidator implements Validator {
    private final UserDetailsServiceImpl userDetails;
    @Autowired
    public UserValidator(UserDetailsServiceImpl userDetails) {
        this.userDetails = userDetails;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        User user = (User)target;
        try {
            userDetails.loadUserByUsername(user.getEmail());
        } catch (UsernameNotFoundException ignored) {
            return; //все ок; пользователь найден
        }
        errors.rejectValue("email","","Человек с такой почтой уже существует");
    }
}
