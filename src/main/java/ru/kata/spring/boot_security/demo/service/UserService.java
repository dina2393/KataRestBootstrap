package ru.kata.spring.boot_security.demo.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import ru.kata.spring.boot_security.demo.models.User;

import java.util.List;

public interface UserService {
    public List<User> getAllUsers();
    public void saveNewUser(User user, String role);
    public void updateUser(User userUpdate, String roleUpdate);
    public void deleteUser(int id);
    public User getUser(int id);
    public User getUserByName(String userName);

}
