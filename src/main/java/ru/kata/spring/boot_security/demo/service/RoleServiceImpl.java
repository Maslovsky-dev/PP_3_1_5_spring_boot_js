package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.repository.RoleRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
@Service
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;
    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Transactional
    @Override
    public List<Role> findAll() {
        return roleRepository.findAll();
    }
    @Transactional
    @Override
    public Optional<Role> findById(String roleName) {
        return roleRepository.findById(roleName);
    }
    @Transactional
    @Override
    public void save(Role role) {
        roleRepository.save(role);
    }
    @Transactional
    @Override
    public void deleteById(String roleName) {
        roleRepository.deleteById(roleName);
    }
}
