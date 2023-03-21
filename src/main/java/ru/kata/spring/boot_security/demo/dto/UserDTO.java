package ru.kata.spring.boot_security.demo.dto;

import ru.kata.spring.boot_security.demo.model.Role;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

public class UserDTO {

    private Long id;
    @NotEmpty(message = "Name should be not empty")
    @Size(min = 2, max = 30, message = "Name wrong size")
    private String firstName;

    @NotEmpty(message = "LastName should be not empty")
    @Size(min = 2, max = 30, message = "Name wrong size")
    private String lastName;

    @Min(value = 1, message = "Age > 0")
    private int age;
    @Email
    @NotEmpty(message = "Email should be not empty")
    private String email;

    @NotEmpty(message = "Password should be not empty")
    private String password;

    @NotEmpty(message = "Не выбрана ни одна роль")
    private Set<Role> roles = new HashSet<>();

    public String getPassword() {
        return password;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
}
