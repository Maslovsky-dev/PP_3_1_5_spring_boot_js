package ru.kata.spring.boot_security.demo.model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "roles")
public class Role {
    @Id
    @Column(name = "role_name")
    private String roleName;

    @ManyToMany(mappedBy = "roles")
    private Set<User> users = new HashSet<>();



    @Override
    public String toString() {
        return "Role{roleName='" + roleName + '\'' +
                '}';
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }


    public Role() {
    }

    public Role(String roleName) {
        this.roleName = roleName;
    }
}

