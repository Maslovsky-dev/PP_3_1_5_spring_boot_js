//package ru.kata.spring.boot_security.demo.service;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//import ru.kata.spring.boot_security.demo.dao.UserDao;
//import ru.kata.spring.boot_security.demo.model.User;
//
//
//import java.util.List;
//@Service
//public class UserServiceImpl implements UserService {
//
//    @Autowired
//    private UserDao userDao;
//
//    @Transactional
//    @Override
//    public void add(User user) {
//        userDao.add(user);
//    }
//
//    @Transactional
//    @Override
//    public User userById(Long id) {
//        return userDao.userById(id);
//    }
//
//    @Transactional(readOnly = true)
//    @Override
//    public List<User> listUsers() {
//        return userDao.listUsers();
//    }
//    @Transactional
//    @Override
//    public void update(User user) {
//        System.out.println("Service: " + user);
//        userDao.update(user);
//    }
//    @Transactional
//    @Override
//    public void delete(Long id) {
//        userDao.delete(id);
//    }
//
//}
