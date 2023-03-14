package ru.kata.spring.boot_security.demo.model;


import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "username")
    @Size(min = 2, max = 100, message = "Имя должно быть от 2 до 100 символов")
    @NotEmpty(message = "Имя не должно быть пустым")
    private String username;


    @Column(name = "email")
    @Size(min = 2, max = 100, message = "Почта должно быть от 2 до 100 символов")
    @NotEmpty (message = "Почта не должна быть пустой")
    private String email;

    @Column(name = "password")
    @NotEmpty(message = "Пароль не должен быть пустым")
    private String password;

    @ManyToMany (cascade = CascadeType.MERGE)
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_name")
    )
    @NotEmpty(message = "Не выбрана ни одна роль")
    private Set<Role> roles = new HashSet<>();

    public User() {
    }

    public User(String username, String email) {
        this.username = username;
        this.email = email;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String firstName) {
        this.username = firstName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", role='" + roles.toString() + '\'' +
                '}';
    }
}
