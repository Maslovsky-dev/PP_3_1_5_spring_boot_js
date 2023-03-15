package ru.kata.spring.boot_security.demo.model;


import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(min = 2, max = 100, message = "Имя должно быть от 2 до 100 символов")
    @NotEmpty(message = "Имя не должно быть пустым")
    private String firstName;

    @Size(min = 2, max = 100, message = "Фамилия должна быть от 2 до 100 символов")
    @NotEmpty(message = "Фамилия не должна быть пустым")
    private String lastName;

    @Min(value = 18, message = "Пользоваться сервисом можно только с 18 лет")
    @Max(value = 150, message = "Возраст превышает допустимый")
    @NotNull(message = "Возраст не должен быть пустым")
    private int age;


    @Size(min = 2, max = 100, message = "Почта должно быть от 2 до 100 символов")
    @NotEmpty (message = "Почта не должна быть пустой")
    private String email;


    @NotEmpty(message = "Пароль не должен быть пустым")
    private String password;

    @ManyToMany (cascade = CascadeType.MERGE,
            fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_name")
    )
    @NotEmpty(message = "Не выбрана ни одна роль")
    private Set<Role> roles = new HashSet<>();

    public User() {
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

    public String getPassword() {
        return password;
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

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", age=" + age +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", roles=" + roles +
                '}';
    }
}
