package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.repository.RoleRepository;
import ru.kata.spring.boot_security.demo.repository.UserRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;


    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }


    @Transactional
    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }


    @Transactional
    @Override
    public void saveNewUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    @Transactional
    @Override
    public void updateUser(User userUpdate, int id) {
        userUpdate.setId(id);
        userUpdate.setPassword(passwordEncoder.encode(userUpdate.getPassword()));
        userRepository.save(userUpdate);
    }

    @Transactional
    @Override
    public void deleteUser(int id) {
        userRepository.deleteById(id);
    }

    @Transactional
    @Override
    public User getUser(int id) {
        Optional<User> foundUser =userRepository.findById(id);
        return foundUser.orElse(null);
    }

    @Transactional
    @Override
    public User getUserByName(String userName) {
        return userRepository.findByUsername(userName);
    }
    @Transactional
    @Override
    public Set<Role> getAllRoles() {
        Set<Role> allRoles =  new HashSet<>(roleRepository.findAll());
        return allRoles;
    }


    @Transactional
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User foundUser = userRepository.findByUsername(username);
        if(foundUser==null) {
            throw new UsernameNotFoundException(String.format("User '%s' not found!", username));
        }
        return new org.springframework.security.core.userdetails.User(foundUser.getUsername(), foundUser.getPassword(), foundUser.getAuthorities());

    }
}
