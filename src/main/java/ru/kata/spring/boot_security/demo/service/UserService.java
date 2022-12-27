package ru.kata.spring.boot_security.demo.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;

import java.util.List;
import java.util.Set;

public interface UserService {
    public List<User> getAllUsers();
    public void saveNewUser(User user);
    public void updateUser(User userUpdate, int id);
    public void deleteUser(int id);
    public User getUser(int id);
    public User getUserByName(String userName);
    Set<Role> getAllRoles();

}
